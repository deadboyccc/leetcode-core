package one029;

class Solution {
    fun twoCitySchedCost(costs: Array<IntArray>): Int {
        // optimize first n/2 for min cost(max profit) of sending people to city A OVER city B
        // [costToA, costToB] [ 10, 20] B-A = 10 ( cost saving/ profit to have )
        // 4 elements , 4/2 = 2 -> 0,1 | 3,4
        val costSaveToA = costs.map { (a, b) -> listOf(a, b, a - b) }.sortedBy { it[2] }
        val half = costSaveToA.size / 2
        return costSaveToA.mapIndexed { index, (costToA, costToB) ->
            if (index < half) costToA else costToB
        }.sum()
//        return costSaveToA.foldIndexed(0) { index, sum, element ->
//            val (costToA, costToB) = element
//            if (index < half) {
//                sum + costToA
//            } else {
//                sum + costToB
//            }
//        }

    }
}