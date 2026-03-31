package misc

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


fun main1(): Unit = runBlocking {
    launch {
        try {
            throw UnsupportedOperationException("Ouch!")
        } catch (u: UnsupportedOperationException) {
            println("Handled $u")
        }
    }
}

fun main2(): Unit = runBlocking {
    launch {
        try {
            while (true) {
                println("Heartbeat!")
                delay(500.milliseconds)
            }
        } catch (e: Exception) {
            println("Heartbeat terminated: $e")
            // catches it and rethrow it
            throw e
        }
    }
    launch {
        delay(1.seconds)
        throw UnsupportedOperationException("Ow!")
    }
}

val exceptionHandler = CoroutineExceptionHandler { context, exception ->
    println("[ERROR] $exception")
}

fun main3(): Unit = runBlocking {
    supervisorScope {
        launch {
            try {
                while (true) {
                    println("Heartbeat!")
                    delay(500.milliseconds)

                }
            } catch (e: Exception) {
                println("Heartbeat terminated: $e")
                throw e
            }
        }
        launch {
            delay(1.seconds)
            throw UnsupportedOperationException("Ow!")
        }
    }
}

class ComponentWithScope2(
    dispatcher: CoroutineDispatcher =
        Dispatchers.Default
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("[ERROR] ${e.message}")
    }

    private val scope = CoroutineScope(
        SupervisorJob() + dispatcher + exceptionHandler
    )

    fun action() = scope.launch {
        throw UnsupportedOperationException("Ouch!")
    }
}

fun main4() = runBlocking {
    val supervisor = ComponentWithScope()
    supervisor.action()
    delay(1.seconds)
}

class ComponentWithScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("[ERROR] Caught in handler: ${e.message}")
    }

    // Using a SupervisorJob ensures one failing child doesn't kill the whole scope
    private val scope = CoroutineScope(
        SupervisorJob() + dispatcher + exceptionHandler
    )

    fun action() = scope.launch {
        // Option A: If you use async, you should usually await it
        val deferred = async {
            throw UnsupportedOperationException("Ouch!")
        }

        try {
            deferred.await()
        } catch (e: Exception) {
            println("[LOCAL CATCH] ${e.message}")
        }
    }
}

fun main5() = runBlocking {
    val supervisor = ComponentWithScope2()
    supervisor.action()
    delay(1.seconds)
}

//
//18.4 Handling errors in flows
class UnhappyFlowException : Exception()

val exceptionalFlow = flow {
    repeat(5) { number ->
        emit(number)
    }
    throw UnhappyFlowException()
}

// main 6
fun main6() = runBlocking {
    val transformedFlow = exceptionalFlow.map {
        it * 2
    }
    try {
        transformedFlow.collect {
            print("$it ")
        }
    } catch (u: UnhappyFlowException) {
        println("\nHandled: $u")
    }
// 0 2 4 6 8
// Handled: UnhappyFlowException
}

fun main7() = runBlocking {
    exceptionalFlow
        .catch { cause ->
            println("\nHandled: $cause")
            emit(-1)
        }
        .collect {
            print("$it ")
        }
}

// 0 1 2 3 4
// Handled: UnhappyFlowException
// -1

// original 0,1,2,3,4 _> 1,2,3,4,5
fun main8() = runBlocking {
    exceptionalFlow
        .map { it + 1 }
        .onEach {
            // This is "upstream" from the .catch below
            throw UnhappyFlowException()
        }
        .catch { cause ->
            // This now successfully catches the UnhappyFlowException
            println("\nHandled: $cause")
        }
        .collect { value ->
            print("$value ")
        }
}

class CommunicationException : Exception("Communicationfailed!")

val unreliableFlow = flow {
    println("Starting the flow!")
    repeat(10) { number ->
        if (Random.nextDouble() < 0.1) throw CommunicationException()
        emit(number)
    }
}

fun main9() = runBlocking {
    unreliableFlow
        .retry(5) { cause ->
            println("\nHandled: $cause")
            cause is CommunicationException
        }
        .collect { number ->
            print("$number ")
        }
}

class PlaygroundTest {
    @Test
    fun testDelay() = runTest {
        val startTime = System.currentTimeMillis()
        delay(20.seconds)
        println(System.currentTimeMillis() - startTime)
// 11
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDelay2() = runTest {
        var x = 0
        launch {
            x++
        }
        launch {
            x++
        }
        runCurrent()
        assertEquals(2, x)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDelay3() = runTest {
        var x = 0
        launch {
            delay(500.milliseconds)
            x++
        }
        launch {
            x++
            delay(1.seconds)
        }
        println(currentTime) // 0
        delay(600.milliseconds)
        assertEquals(1, x)
        println(currentTime) // 600
        delay(500.milliseconds)
        assertEquals(2, x)
        println(currentTime) // 1100
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDelayFinale() = runTest {
        var x = 0
        launch {
            x++
            launch {
                x++
            }
        }
        launch {
            x++
            delay(200.milliseconds)
        }
        runCurrent()
        assertEquals(2, x)
        advanceUntilIdle()
        assertEquals(3, x)
    }
}
