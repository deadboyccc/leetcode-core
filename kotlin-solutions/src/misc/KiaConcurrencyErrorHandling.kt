package misc

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


fun main1(): Unit = runBlocking {
    launch {
        try {
            throw UnsupportedOperationException("Ouch!")
        } catch (u: UnsupportedOperationException) {
            println("Handled $u")
        }
    }
}

fun main2(): Unit = runBlocking {
    launch {
        try {
            while (true) {
                println("Heartbeat!")
                delay(500.milliseconds)
            }
        } catch (e: Exception) {
            println("Heartbeat terminated: $e")
            // catches it and rethrow it
            throw e
        }
    }
    launch {
        delay(1.seconds)
        throw UnsupportedOperationException("Ow!")
    }
}

val exceptionHandler = CoroutineExceptionHandler { context, exception ->
    println("[ERROR] $exception")
}

fun main3(): Unit = runBlocking {
    supervisorScope {
        launch {
            try {
                while (true) {
                    println("Heartbeat!")
                    delay(500.milliseconds)

                }
            } catch (e: Exception) {
                println("Heartbeat terminated: $e")
                throw e
            }
        }
        launch {
            delay(1.seconds)
            throw UnsupportedOperationException("Ow!")
        }
    }
}

class ComponentWithScope2(
    dispatcher: CoroutineDispatcher =
        Dispatchers.Default
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("[ERROR] ${e.message}")
    }

    private val scope = CoroutineScope(
        SupervisorJob() + dispatcher + exceptionHandler
    )

    fun action() = scope.launch {
        throw UnsupportedOperationException("Ouch!")
    }
}

fun main4() = runBlocking {
    val supervisor = ComponentWithScope()
    supervisor.action()
    delay(1.seconds)
}

class ComponentWithScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("[ERROR] Caught in handler: ${e.message}")
    }

    // Using a SupervisorJob ensures one failing child doesn't kill the whole scope
    private val scope = CoroutineScope(
        SupervisorJob() + dispatcher + exceptionHandler
    )

    fun action() = scope.launch {
        // Option A: If you use async, you should usually await it
        val deferred = async {
            throw UnsupportedOperationException("Ouch!")
        }

        try {
            deferred.await()
        } catch (e: Exception) {
            println("[LOCAL CATCH] ${e.message}")
        }
    }
}

fun main5() = runBlocking {
    val supervisor = ComponentWithScope2()
    supervisor.action()
    delay(1.seconds)
}

//
//18.4 Handling errors in flows