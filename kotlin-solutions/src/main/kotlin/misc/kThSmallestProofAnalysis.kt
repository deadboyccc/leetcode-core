package misc.kThSmallestProofAnalysis.practie

fun main() {

    val left = 19
    val right = 20
    // so this is left biased, if we say left = mid, and we are left-biased towards left in the int division we will be stuck in infinite loop
    // Rule of thumb : left = mid ? right biased-> so we are approaching right with our left,mid || right = mid? left biased, we are approaching left with our mid and right
    val mid = ((left + right) / 2).also { println(it) }

    // I think framing it in the strictlyLess is easier as in, if we want to find the kTh smallest
    // we want k-1 elements less than it, even if the matrix is not distinct
    // we want to find the number that countStrictlyLess = k-1
    // if w want the 3rd smallest in [10,33,44,100], it's simply 44 because k-1(2) elements are less than it
    // it's  easier to frame it this way and makes the range based binary search easier

    // let's mimic the algorithm on a 1D sorted Array
    val arr = intArrayOf(10, 20, 30, 40, 50, 60, 70, 80)
    // we want to find the 5th smallest-> k-1 =4 elements less than it
    val k = 5
    findKthSmallest(arr, k).also { println(it) }

}

fun findKthSmallest(arr: IntArray, k: Int): Int {
    var left = arr.first()
    var right = arr.last()

    while (left < right) {
        // 1. Calculate mid (prevents integer overflow)
        val mid = left + (right - left) / 2

        // 2. Count elements LESS THAN OR EQUAL to mid
        val count = arr.count { it <= mid }

        // 3. The standard condition check
        if (count < k) {
            // If the count is less than k, mid is too small.
            // We must search strictly to the right.
            left = mid + 1
        } else {
            // If count >= k, mid could be the answer, or the answer is smaller.
            // We include mid in our next search space.
            right = mid
        }
    }

    // left and right converge on the exact answer
    return left
}
