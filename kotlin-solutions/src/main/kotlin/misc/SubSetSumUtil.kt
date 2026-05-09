package misc.subset

// all unique sums reachable by any non-empty subset
fun subsetSums(nums: IntArray): IntArray =
    nums.fold(mutableSetOf<Int>()) { sums, num ->
        val snapshot = sums.toList()          // freeze before mutation
        sums.add(num)                          // subset containing only num
        snapshot.forEach { sums.add(it + num) } // extend each prior subset
        sums
    }.toIntArray()

// how many distinct subsets produce each sum
fun subsetSumFrequencies(nums: IntArray): Map<Int, Int> =
    nums.fold(mutableMapOf<Int, Int>()) { freq, num ->
        val snapshot = freq.keys.toList()      // freeze before mutation
        freq.merge(num, 1, Int::plus)          // subset containing only num
        snapshot.forEach { prev ->
            freq.merge(prev + num, 1, Int::plus) // extend each prior subset
        }
        freq
    }

fun main() {
    val nums = intArrayOf(1, 2, 3, 2)

    subsetSums(nums)
        .sorted()
        .also { println("Distinct subset sums : $it") }

    subsetSumFrequencies(nums)
        .entries
        .sortedBy { it.key }
        .also { entries ->
            println("Sum frequencies:")
            entries.forEach { (sum, count) -> println("  sum=$sum → $count subset(s)") }
        }

    // try the merge() and entries + sorted by chained
    // num -> num*2
    val testMap = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).associateWith { it * 2 }.toMutableMap()

    // merge: double the value of existing keys
    testMap.keys.toList().forEach { k ->
        testMap.merge(k, k * 2) { old, _ -> old * 2 }
    }

// entries + sortedBy chained
    testMap.entries
        .sortedBy { it.value }
        .forEach { (key, value) ->
            println("${key.toString().padEnd(3)} -> $value")
        }
}
