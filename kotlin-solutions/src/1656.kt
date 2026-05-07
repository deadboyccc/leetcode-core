package one656

class OrderedStream(n: Int) {
    // start of the stream = 1
    private var ptr = 1

    // [0,1,..,n-1,n]
    // id is between [1,n]
    // stream[idKey] = String
    private val stream = arrayOfNulls<String>(n + 1)

    fun insert(idKey: Int, value: String): List<String> {
        // we add the current pair to the stream
        stream[idKey] = value

        // we build res by adding ptr if not null and ptr++ as long as not null
        val res = mutableListOf<String>()
        while (stream[ptr] != null) {
            stream[ptr]?.let { res.add(it) }

            // update ptr as long as it doesn't pass the lastIndex of the Stream
            if (++ptr > stream.lastIndex) {
                break
            }
        }
        return res
    }

}

// claude
class OptimizedClaude {
    class OrderedStream(n: Int) {
        private var ptr = 1
        private val stream = arrayOfNulls<String>(n + 1)

        fun insert(idKey: Int, value: String): List<String> {
            stream[idKey] = value
            val start = ptr
            while (ptr <= stream.lastIndex && stream[ptr] != null) ptr++
            return if (ptr > start) stream.slice(start until ptr).filterNotNull() else emptyList()
        }
    }
}

// gpt wins : )
class OptimizedGPT {
    class OrderedStream(n: Int) {

        private val stream = arrayOfNulls<String>(n)
        private var ptr = 0

        fun insert(idKey: Int, value: String): List<String> {
            stream[idKey - 1] = value

            if (idKey - 1 != ptr) return emptyList()

            val result = mutableListOf<String>()

            while (ptr < stream.size && stream[ptr] != null) {
                result.add(stream[ptr]!!)
                ptr++
            }

            return result
        }
    }
}

