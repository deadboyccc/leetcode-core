package misc

data class Person(val firstName: String, val lastName: String, val age: Int)

fun main() {
    val people = listOf(
        Person("Alice", "Smith", 34),
        Person("Bob", "Jones", 22),
        Person("Charlie", "Brown", 22),
        Person("Diana", "Prince", 45),
        Person("Eve", "Adams", 34),
    )

    // --- fold: accumulate a summary string (mirrors floydWarshall's matrix fold) ---
    val summary = people.fold("Roster: ") { acc, p -> "$acc${p.firstName} " }
    println(summary)
    val anotherSummary = people.fold(0) { acc, person -> acc + person.age }
    println("total age : $anotherSummary")
    // aggregate sum of all properties
    val notherSummary2 = people.fold(Triple("", "", 0)) { accTriple, person ->
        Triple(
            accTriple.first + person.firstName + " ",
            accTriple.second + person.lastName + " ",
            accTriple.third + person.age
        )
    }
    println("total aggregate : $notherSummary2")

    val anotherSummary3 = people.fold(listOf<String>()) { acc, person ->
        val str = person.firstName + " " + person.lastName + " " + person.age + " ----> "
        acc + str
    }
    println("total names total string : $anotherSummary3")

    // --- groupBy + mapValues (mirrors buildMatrix's edge deduplication) ---
    val byAge = people
        .groupBy { it.age }
        .mapValues { (_, group) -> group.map { it.firstName } }
    println("By age: $byAge")

    val byAge2 = people.groupBy { it.age }.mapValues { (a, values) -> values.map { it.firstName + " " + a } }
    println("By age2: $byAge2")
    // --- Triple + destructuring (mirrors edge representation) ---
    // first, first, weight ? strength of relationship xD  + or distance
    val relationships = listOf(
        Triple("Alice", "Bob", 5),
        Triple("Bob", "Charlie", 3),
        Triple("Alice", "Charlie", 5),
    )
    val minDistance = relationships
        .groupBy({ it.first to it.second }, { it.third.toBigDecimal() })
        .mapValues { (_, ds) -> ds.min() }
    println("Min distances: $minDistance")

    // --- flatMap (mirrors undirected edge expansion) ---
    val undirected = relationships
        .flatMap { (a, b, w) -> listOf(Triple(a, b, w), Triple(b, a, w)) }
    println("Undirected: $undirected")

    // --- let chaining (mirrors findTheCity's pipeline) ---
    val oldest = people
        .let { list -> list.filter { it.age > 30 } }
        .let { filtered -> filtered.maxByOrNull { it.age } }
        .let { person -> person?.firstName ?: "nobody" }
    println("Oldest over 30: $oldest")

    // --- indices.fold on 2D structure (mirrors floydWarshall directly) ---
    // score[i][j] = how well person i and person j "know" each other (mock data)
    val scores = Array(3)
    { i ->
        IntArray(3)
        { j -> if (i == j) 0 else i + j }
    }
    println("scores: ${scores.contentToString()}")
    val relaxed = scores.indices.fold(scores) { currScores, score ->
        Array(currScores.size) { i ->
            IntArray(currScores.size) { j ->
                minOf(currScores[i][j], currScores[i][score] + currScores[score][j])
            }
        }
    }
    println("Relaxed scores: ${relaxed.map { it.toList() }}")

    // --- in range check (mirrors neighbor count filter) ---
    val threshold = 25..40
    val inRange = people.filter { it.age in threshold }
    println("Age in $threshold: ${inRange.map { it.firstName }}")

    // --- downTo + minByOrNull for tie-breaking (mirrors findTheCity) ---
    val youngestHighestIndex = (people.indices.last downTo 0)
        .minByOrNull { people[it].age }
        .let { people[it!!] }
    println("Youngest, highest index on tie: ${youngestHighestIndex.firstName}")

    // --- fold tie-breaking without !! (mirrors final fold in findTheCity) ---
    val oldestLastWins = people.fold(people.first()) { best, p ->
        if (p.age >= best.age) p else best
    }
    println("Oldest, last wins on tie: ${oldestLastWins.firstName}")

    // --- any / all / count (mirrors hasNegativeCycle) ---
    println("Any under 25: ${people.any { it.age < 25 }}")
    println("All adults:   ${people.all { it.age >= 18 }}")
    println("Over 30:      ${people.count { it.age > 30 }}")
}
