package misc

import kotlinx.coroutines.*
import java.util.*
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.milliseconds

/**
 * --- GRAPH VISUALIZATION ---
 * The BFS frontiers expand from both ends, colliding in the Middle Zone (Layer 3).
 * Each bracket [ ] represents a layer of nodes.
 *
 * SEARCH START (Forward) →                      ← SEARCH TARGET (Backward)
 *
 * [ A B C D E ]  <-- Layer 1 (Start Zone)
 * ||
 * [ F G H I J ]  <-- Layer 2 (Transition)
 * ||
 * [ K L M N O ]  <-- Layer 3 (Likely Collision Zone)
 * ||
 * [ P Q R S T ]  <-- Layer 4 (Transition)
 * ||
 * [ U V W X Y Z ] <-- Layer 5 (Target Zone)
 *
 * Strategic Bridges (Shortcuts):
 * - A -> K (Jump to middle)
 * - M -> Z (Jump to end)
 */

fun main() = runBlocking {
    val solver = BidirectionalSearch(generateLayeredGraph())

    println("Starting Bidirectional BFS on thread: ${Thread.currentThread().name}")

    // runSearch will suspend until a collision is found by one of the background threads
    val meetingNode = solver.runSearch(startNode = 'A', targetNode = 'Z')

    println("\n[FINAL RESULT] The two searches met at Node: '$meetingNode'")
}

/**
 * Encapsulates the logic for running two concurrent BFS searches.
 */
class BidirectionalSearch(private val adj: Map<Char, List<Char>>) {

    // Shared thread-safe map to track visits: Node -> "START" or "TARGET"
    private val visited = ConcurrentHashMap<Char, String>()

    // A thread-safe way to communicate the first meeting point found back to the caller
    private val meetingPoint = CompletableDeferred<Char>()

    /**
     * Orchestrates the two parallel searches using structured concurrency.
     */
    suspend fun runSearch(startNode: Char, targetNode: Char): Char = coroutineScope {

        // 1. Launch Forward Search on a background thread pool
        launch(Dispatchers.Default) {
            performBfs(startNode, "START", "TARGET")
        }

        // 2. Launch Backward Search on a background thread pool
        launch(Dispatchers.Default) {
            performBfs(targetNode, "TARGET", "START")
        }

        // 3. Suspend here until one of the coroutines calls meetingPoint.complete(char)
        val winner = meetingPoint.await()

        // 4. Cancel the entire scope immediately to stop the "losing" search thread
        this@coroutineScope.cancel()

        winner
    }

    /**
     * Standard BFS implementation modified for concurrency.
     */
    private suspend fun performBfs(start: Char, myDir: String, otherDir: String) {
        val queue: Queue<Char> = LinkedList()
        queue.add(start)
        visited[start] = myDir

        // Continue only if the queue has items AND no one has found the meeting point yet
        while (queue.isNotEmpty() && !meetingPoint.isCompleted) {
            val current = queue.poll() ?: continue

            // Simulate network/processing latency to make the race visible
            delay(50.milliseconds)

            adj[current]?.forEach { neighbor ->
                // putIfAbsent is ATOMIC:
                // It returns the existing value if present, or null if it just successfully added ours.
                val alreadyVisitedBy = visited.putIfAbsent(neighbor, myDir)

                when (alreadyVisitedBy) {
                    null -> {
                        // Node was unvisited. We claimed it.
                        queue.add(neighbor)
                        println("[$myDir] Visited $neighbor on ${Thread.currentThread().name}")
                    }

                    otherDir -> {
                        // COLLISION! The other search reached this node before/simultaneously with us.
                        if (meetingPoint.complete(neighbor)) {
                            println("[$myDir] FOUND COLLISION at '$neighbor'!")
                        }
                    }

                    // If alreadyVisitedBy == myDir, we just ignore it (already in our queue)
                }
            }
        }
    }
}

/**
 * Idiomatic generation of the Adjacency Map using functional builders.
 */
fun generateLayeredGraph(): Map<Char, List<Char>> = mutableMapOf<Char, MutableList<Char>>().apply {
    fun addEdge(u: Char, v: Char) {
        this.getOrPut(u) { mutableListOf() }.add(v)
        this.getOrPut(v) { mutableListOf() }.add(u)
    }

    // Creating 5 distinct layers of characters
    val layers = listOf('A'..'E', 'F'..'J', 'K'..'O', 'P'..'T', 'U'..'Z').map { it.toList() }

    // Connect layer i to layer i+1
    for (i in 0 until layers.size - 1) {
        val currentLayer = layers[i]
        val nextLayer = layers[i + 1]
        for (u in currentLayer) {
            for (v in nextLayer) {
                // Determine connections based on Char codes to create a consistent web
                if ((u.code + v.code) % 2 == 0) addEdge(u, v)
                if ((u.code * v.code) % 3 == 0) addEdge(u, v)
            }
        }
    }

    // Strategic Bridges to test race conditions and non-linear meeting points
    addEdge('A', 'K') // Forward search can jump deep into the middle early
    addEdge('M', 'Z') // Backward search can jump deep into the middle early

}.mapValues { it.value.distinct().toList() }

/*
 * =========================================================================================
 * DEEP DIVE: HOW THIS CODE WORKS
 * =========================================================================================
 * * 1. THE ARCHITECTURE: BIDIRECTIONAL SEARCH
 * ----------------------------------------
 * Traditional BFS expands like a circle from one point. Bidirectional BFS expands two
 * smaller circles from both the start and the end. Mathematically, two small circles
 * have much less area (search space) than one giant circle, making this highly efficient.
 *
 * 2. CONCURRENCY MODEL
 * --------------------
 * We use Kotlin Coroutines. 'Dispatchers.Default' ensures that if your CPU has multiple
 * cores, these two searches literally run at the same nanosecond on different threads.
 *
 * -----------------------------------------------------------------------------------------
 * LINE-BY-LINE BREAKDOWN
 * -----------------------------------------------------------------------------------------
 *
 * [STATE MANAGEMENT]
 * - 'visited': A ConcurrentHashMap. Unlike a standard HashMap, this allows thread A and
 * thread B to check/write keys simultaneously without causing a 'ConcurrentModificationException'.
 * - 'meetingPoint': A CompletableDeferred. Think of this as a "Future" that we manually
 * trigger. The first search to find a neighbor belonging to the "other side" calls
 * .complete(), which "wakes up" the main thread.
 *
 * [runSearch() LOGIC]
 * - 'coroutineScope { ... }': This provides "Structured Concurrency." If the forward search
 * crashes, the backward search is automatically cancelled.
 * - 'this@coroutineScope.cancel()': This is a "Qualified This." It tells Kotlin: "I want to
 * cancel the scope that runSearch created, effectively stopping all background jobs
 * associated with this specific search."
 *
 * [performBfs() & THE ATOMIC RACE]
 * - 'visited.putIfAbsent(neighbor, myDir)': This is the core of the race condition handling.
 * - Thread A (Forward) and Thread B (Backward) might both reach Node 'M' at the same time.
 * - 'putIfAbsent' is an atomic operation provided by the JVM. Only one thread will get 'null'
 * back (the winner). The second thread will get the identity of the winner back.
 * - 'when (alreadyVisitedBy)':
 * - If it's 'null', we "own" the node.
 * - If it's 'otherDir', we just hit the frontier of the other search. Collision!
 *
 * [graphBuilder]
 * - Uses '.apply' and 'getOrPut' to build a complex data structure cleanly.
 * - '.mapValues' at the end ensures that the resulting adjacency list is immutable,
 * preventing accidental changes during the search.
 * =========================================================================================
 */
