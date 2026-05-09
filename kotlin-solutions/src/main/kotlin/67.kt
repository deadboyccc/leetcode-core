package six7

import java.math.BigInteger

// === OPTIMAL SIMULATION (Interview Standard) ===
// Logic: Mimics manual paper-and-pencil addition from right to left.
// Complexity: Time O(N), Space O(N) for the result string.
class OptimalSolution {
    fun addBinary(a: String, b: String): String {
        val res = StringBuilder()
        var i = a.length - 1
        var j = b.length - 1
        var carry = 0

        while (i >= 0 || j >= 0 || carry > 0) {
            val sum = (if (i >= 0) a[i--] - '0' else 0) +
                    (if (j >= 0) b[j--] - '0' else 0) + carry
            res.append(sum % 2)
            carry = sum / 2
        }
        return res.reverse().toString()
    }
}

// === FUNCTIONAL CONVERSION (Using shl/shiftLeft) ===
// Logic: Converts strings to BigIntegers using bit-shifting, adds them, and converts back.
// Complexity: Time O(N) to parse, but BigInteger operations have higher overhead.
class ConversionSolution {
    fun addBinary(a: String, b: String): String {
        fun toBigInt(s: String) = s.foldIndexed(BigInteger.ZERO) { i, total: BigInteger, char ->
            if (char == '1') total.add(BigInteger.ONE.shiftLeft(s.length - 1 - i))
            else total
        }
        return toBigInt(a).add(toBigInt(b)).toString(2)
    }
}

// === HARDWARE LOGIC (Bitwise XOR & AND) ===
// Logic: Uses XOR to find the sum bits and AND to find the carry bits.
// Complexity: Time O(Number of carries), Space O(N).
class BitwiseSolution {
    fun addBinary(a: String, b: String): String {
        var x = BigInteger(a, 2)
        var y = BigInteger(b, 2)
        val zero = BigInteger.ZERO

        while (y != zero) {
            val answer = x.xor(y)             // XOR acts as a "Half Adder" (sum without carry)
            val carry = x.and(y).shiftLeft(1) // AND finds where both bits are 1 (generates carry)
            x = answer
            y = carry
        }
        return x.toString(2)
    }
}
