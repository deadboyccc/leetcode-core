package one672;

class Solution {
    fun maximumWealth(accounts: Array<IntArray>): Int {
        // accounts[i][j] = i-th customer and j-th bank (irrelevant)
        return (accounts.map { it.sum() }).max()

    }
}