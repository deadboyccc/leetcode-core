package seven33;

class Solution {
    fun floodFill(image: Array<IntArray>, sr: Int, sc: Int, color: Int): Array<IntArray> {

        val directions = listOf<Pair<Int, Int>>(0 to 1, 0 to -1, 1 to 0, -1 to 0)
        val q = ArrayDeque<Pair<Int, Int>>().apply { addLast(sr to sc) }
        val originalColor = image[sr][sc]
        val visited = hashSetOf<Pair<Int, Int>>()
        while (q.isNotEmpty()) {

            val (currX, currY) = q.removeFirst()
            if (visited.contains(currX to currY)) continue
            visited.add(currX to currY)


            for ((xOffset, yOffset) in directions) {
                val (neighborX, neighborY) = xOffset + currX to yOffset + currY
                if (neighborX !in image.indices || neighborY !in image[0].indices) continue
                if (image[neighborX][neighborY] == originalColor) q.addLast(neighborX to neighborY)
            }
            image[currX][currY] = color
        }


        return image
    }
}