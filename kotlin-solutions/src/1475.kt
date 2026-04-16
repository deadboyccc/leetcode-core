package one475;

class Solution {

    fun finalPrices(prices: IntArray): IntArray {
        val res = prices.copyInto(IntArray(prices.size), 0, 0, prices.size)
        // index stack
        val stack = ArrayDeque<Int>()
//       while stack is not empty and current price is smaller or equal that price on the stack
//        res[ index of the stack ] -= price[i]
//      8,9, 10, 11 prices
//      0,1,2 ,3,4 stack
        for (i in prices.indices) {
            while (!stack.isEmpty() && prices[stack.last()] >= prices[i]) {
                res[stack.removeLast()] -= prices[i]
            }
            stack.add(i)
        }
        return res

    }
}