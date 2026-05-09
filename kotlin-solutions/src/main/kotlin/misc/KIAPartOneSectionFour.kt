package kia.part1.sec4

import kotlin.random.Random

interface User {
    val nickname: String
}

/*
PrivateUser: Data is passed in and stored.

SubscribingUser: Data is calculated from another property every time.

SocialUser: Data is calculated once at the start and then stored.
 */
class PrivateUser(override val nickname: String) : User

class SubscribingUser(val email: String) : User {
    override val nickname: String
        get() = email.substringBefore('@')
}

class SocialUser(val accountId: Int) : User {
    override val nickname = getNameFromSocialNetwork(accountId)
}

fun getNameFromSocialNetwork(accountId: Int) = "kodee$accountId"
interface EmailUser {
    val email: String
    val nickname: String
        get() = email.substringBefore('@')
}

class FieldUser(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println(
                """
                Address was changed for $name:
                "$field" -> "$value".
                """.trimIndent()
            )
            field = value
        }
}

class Person(var birthYear: Int) {
    var ageIn2050
        get() = 2050 - birthYear
        set(value) {
            birthYear = 2050 - value
        }
}

class LengthCounter {
    var counter: Int = 0
        private set

    fun addWord(word: String) {
        counter += word.length
    }
}

@OptIn(ExperimentalStdlibApi::class)
class LateInitDemo {
    lateinit var name: String

    init {
        println("init")
        name = Random.nextBytes(8).toHexString()
    }
}

data class Customer(val name: String, val postalCode: Int)

fun main() {
    println(PrivateUser("kodee").nickname)
    // Output: kodee

    println(SubscribingUser("test@kotlinlang.org").nickname)
    // Output: test

    println(SocialUser(123).nickname)
    // Output: kodee123


    val fieldUser = FieldUser("Alice")
    fieldUser.address = "123 Kotlin Lane"

    val lengthCounter = LengthCounter()
    lengthCounter.addWord("Hi!")
    println(lengthCounter.counter)
    // 3
    val late = LateInitDemo().name
    println(late)
    val c1 = Customer("Sam", 11521)
    val c2 = Customer("Mart", 15500)
    val c3 = Customer("Sam", 11521)
    println(c1)
// Customer(name=Sam, postalCode=11521)
    println(c1 == c2)
// false
    println(c1 == c3)
// true
    println(c1.hashCode())
// 2580770
    println(c3.hashCode())
    val c4 = c1.copy(name = "newName")
    println(c4)

}
