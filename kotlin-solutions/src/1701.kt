package one701

/*
There is a restaurant with a single chef. You are given an array customers, where customers[i] = [arrivali, timei]:

// bro what tf is non-decreasing XD
arrivali is the arrival time of the ith customer. The arrival times are sorted in increasing! order.
timei is the time needed to prepare the order of the ith customer.
 */
class Solution {
    fun averageWaitingTime(customers: Array<IntArray>): Double {
        var sum = 0.0
        var chefFreeAt = 0

        customers.forEach { (arrival, prep) ->
            chefFreeAt = maxOf(chefFreeAt, arrival) + prep
            sum += chefFreeAt - arrival
        }

        return sum / customers.size
    }
}