package eight53


class Solution {
    // s = d/t -> t = d/s

    fun carFleet(target: Int, position: IntArray, speed: IntArray): Int {
        val cars = (position zip speed).sortedByDescending { it.first }

        var fleetCount = 0
        var currFleetTime = 0.0

        for ((pos, spd) in cars) {
            val time = (target - pos).toDouble() / spd
            if (time > currFleetTime) {
                fleetCount++
                currFleetTime = time
            }
        }

        return fleetCount
    }
}