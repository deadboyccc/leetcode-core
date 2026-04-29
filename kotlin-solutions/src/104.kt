package one04

import kotlin.math.max

/**
 * Example:
 * var ti = TreeNode(5)
 * var v = ti.`val`
 * Definition for a binary tree node.
 */
class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

class Solution {
    fun maxDepth(root: TreeNode?): Int {
        if (root == null) return 0

        return 1 + maxOf(maxDepth(root.left), maxDepth(root.right))
    }
}

fun main() {
    maxOf(10, 20).also(::println)
    max(10, 20).also(::println)
}