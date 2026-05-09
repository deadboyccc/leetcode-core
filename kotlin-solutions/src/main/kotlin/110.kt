package one10;

import kotlin.math.abs

/**
 * Example:
 * var ti = TreeNode(5)
 * var v = ti.`val`
 * Definition for a binary tree node.
 * }
 */
class Solution {
    fun isBalanced(root: TreeNode?): Boolean {
        if (root == null) return true

        val heightDiff = abs(maxDepth(root.left) - maxDepth(root.right))

        return heightDiff <= 1 && isBalanced(root.left) && isBalanced(root.right)
    }

    fun maxDepth(root: TreeNode?): Int {
        if (root == null) return 0
        return 1 + maxOf(maxDepth(root.left), maxDepth(root.right))
    }

}

class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}
