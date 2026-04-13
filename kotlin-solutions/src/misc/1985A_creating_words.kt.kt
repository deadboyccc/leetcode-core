package misc.cf

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class FastReader(`in`: java.io.InputStream) {
    private val reader: BufferedReader = BufferedReader(InputStreamReader(`in`))
    private var tokenizer: StringTokenizer? = null

    operator fun next(): String? {
        while (tokenizer == null || !tokenizer!!.hasMoreElements()) {
            val line = reader.readLine() ?: return null
            tokenizer = StringTokenizer(line)
        }
        return tokenizer!!.nextToken()
    }

    fun nextInt(): Int = next()!!.toInt()
}

fun main() {
    val fr = FastReader(System.`in`)
    val out = java.io.PrintWriter(System.`out`)

    val tString = fr.next()
    if (tString != null) {
        val t = tString.toInt()
        repeat(t) {
            val s1 = fr.next()!!
            val s2 = fr.next()!!

            val res1 = s2[0] + s1.substring(1)
            val res2 = s1[0] + s2.substring(1)

            out.println("$res1 $res2")
        }
    }
    out.flush()
}
