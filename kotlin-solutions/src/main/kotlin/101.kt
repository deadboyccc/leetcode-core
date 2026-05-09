package one01

class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

/**
 * STRATEGY 1: Brute Force (Copy & Compare)
 * Logic: Create a mirrored copy of the tree and compare it to the original.
 * Time: O(n) - Visits all nodes to copy, then all nodes to compare.
 * Space: O(n) - Creates a brand new tree in memory.
 */
class SolutionBruteForce {
    fun isSymmetric(root: TreeNode?): Boolean {
        val mirroredCopy = reverseAndCopy(root)
        return areTreesEqual(root, mirroredCopy)
    }

    private fun reverseAndCopy(root: TreeNode?): TreeNode? {
        if (root == null) return null
        return TreeNode(root.`val`).apply {
            left = reverseAndCopy(root.right)
            right = reverseAndCopy(root.left)
        }
    }

    private fun areTreesEqual(r1: TreeNode?, r2: TreeNode?): Boolean {
        if (r1 == null && r2 == null) return true
        if (r1 == null || r2 == null) return false
        return r1.`val` == r2.`val` &&
                areTreesEqual(r1.left, r2.left) &&
                areTreesEqual(r1.right, r2.right)
    }
}

/**
 * STRATEGY 2: Optimized Iterative BFS (Symmetry Check)
 * Logic: Uses a Queue to compare mirrored pairs (Outer vs Inner) level-by-level.
 * Time: O(n) - Visits each node once; returns false immediately on mismatch.
 * Space: O(w) - 'w' is the max width of the tree (size of the queue).
 */
class SolutionOptimizedSymmetry {
    fun isSymmetric(root: TreeNode?): Boolean {
        if (root == null) return true

        val queue = ArrayDeque<TreeNode?>()
        queue.add(root.left)
        queue.add(root.right)

        while (queue.isNotEmpty()) {
            val left = queue.removeFirst()
            val right = queue.removeFirst()

            if (left == null && right == null) continue
            if (left == null || right == null || left.`val` != right.`val`) return false

            // Add mirrored pairs: Outer children, then Inner children
            queue.add(left.left)   // Match with...
            queue.add(right.right) // ...this

            queue.add(left.right)  // Match with...
            queue.add(right.left)  // ...this
        }
        return true
    }
}

/**
 * STRATEGY 3: Optimized Iterative BFS (Identity Check)
 * Logic: Checks if two separate trees are identical (not mirrored).
 * Time: O(min(n, m)) - Stops at the first mismatch between trees.
 * Space: O(min(w1, w2)) - Proportional to the width of the trees.
 */
class SolutionOptimizedEquality {
    fun isSameTree(r1: TreeNode?, r2: TreeNode?): Boolean {
        val queue = ArrayDeque<TreeNode?>()
        queue.add(r1)
        queue.add(r2)

        while (queue.isNotEmpty()) {
            val n1 = queue.removeFirst()
            val n2 = queue.removeFirst()

            if (n1 == null && n2 == null) continue
            if (n1 == null || n2 == null || n1.`val` != n2.`val`) return false

            // Add children in identical order (left-left, right-right)
            queue.add(n1.left)
            queue.add(n2.left)
            queue.add(n1.right)
            queue.add(n2.right)
        }
        return true
    }
}
