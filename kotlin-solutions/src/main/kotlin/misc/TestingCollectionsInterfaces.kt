package misc.testingCollectionsInterfaces

fun main() {
    val n = 8

    // Using a mid-dot for a cleaner empty look
    val board = Array(n) { CharArray(n) { '·' } }

    // Define border components
    val topBorder = "┌───" + "┬───".repeat(n - 1) + "┐"

    val middleBorder = "├───" + "┼───".repeat(n - 1) + "┤"

    val bottomBorder = "└───" + "┴───".repeat(n - 1) + "┘"

    println(topBorder)

    board.forEachIndexed { index, row ->
        // Format each cell with padding
        val rowString = row.joinToString(" │ ", prefix = "│ ", postfix = " │")
        println(rowString)

        // Print middle separator except after the last row
        if (index < n - 1) {
            println(middleBorder)
        }
    }

    println(bottomBorder)
}
