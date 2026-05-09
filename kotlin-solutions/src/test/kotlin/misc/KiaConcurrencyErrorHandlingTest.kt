package misc

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class PlaygroundTest {
    @Test
    fun testDelay() = runTest {
        val startTime = System.currentTimeMillis()
        delay(20.seconds)
        println(System.currentTimeMillis() - startTime)
// 11
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDelay2() = runTest {
        var x = 0
        launch {
            x++
        }
        launch {
            x++
        }
        runCurrent()
        assertEquals(2, x)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDelay3() = runTest {
        var x = 0
        launch {
            delay(500.milliseconds)
            x++
        }
        launch {
            x++
            delay(1.seconds)
        }
        println(currentTime) // 0
        delay(600.milliseconds)
        assertEquals(2, x)
        println(currentTime) // 600
        delay(500.milliseconds)
        assertEquals(2, x)
        println(currentTime) // 1100
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDelayFinale() = runTest {
        var x = 0
        launch {
            x++
            launch {
                x++
            }
        }
        launch {
            x++
            delay(200.milliseconds)
        }
        runCurrent()
        assertEquals(3, x)
        advanceUntilIdle()
        assertEquals(3, x)
    }
}
