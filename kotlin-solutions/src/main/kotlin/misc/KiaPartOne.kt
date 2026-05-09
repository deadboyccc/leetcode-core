package misc.kia_part_one

import java.io.File

fun main1() {
    println("12.345-6.A".split(".", "-"))
}

class User(val id: Int, val name: String, val address: String)

fun saveUser(user: User) {
    if (user.name.isEmpty()) {
        throw IllegalArgumentException(
            "Can't save user ${user.id}: empty Name"
        )
    }
    if (user.address.isEmpty()) {
        throw IllegalArgumentException(
            "Can't save user ${user.id}: empty Address"
        )
    }
// Save user to the database
}

fun saveUser2(user: User) {
    fun validate(
        user: User,
        value: String,
        fieldName: String
    ) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user ${user.id}: empty $fieldName"
            )
        }
    }
    validate(user, user.name, "Name")
    validate(user, user.address, "Address")
// Save user to the database
}

// we are delegating the Collection API to the innerList mutableListOf()
/**
 * A Decorator for a MutableCollection that tracks the total number of
 * "add" attempts, including duplicates that were not stored.
 */
class CountingSet<T>(
    private val innerSet: MutableSet<T> = hashSetOf()
) : MutableCollection<T> by innerSet {

    // Tracks every object passed to 'add' or 'addAll'
    var objectsAdded = 0
        private set // Allow public reading, but only internal modification

    /**
     * Increments the counter for a single addition.
     */
    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    /**
     * Increments the counter by the size of the collection provided.
     */
    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}

class DelegatingCollection<T>(
    innerList: Collection<T> = mutableListOf<T>()
) : Collection<T> by innerList

object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(file1: File, file2: File): Int {
        return file1.path.compareTo(
            file2.path,
            ignoreCase = true
        )
    }
}


fun main() {
    saveUser(User(1, "", ""))

    val cset = CountingSet<Int>()

    // We add three items, but only two are unique (1 and 2)
    cset.addAll(listOf(1, 1, 2))

    println("Added ${cset.objectsAdded} objects, ${cset.size} uniques.")
    // Output: Added 3 objects, 2 uniques.
    println("*".repeat(20))

    println(
    )
    CaseInsensitiveFileComparator.compare(
        File("/User"), File("/user")
    )
// 0
    val files = listOf(File("/Z"), File("/a"))
    println(
        files.sortedWith(
            CaseInsensitiveFileComparator
        )
    )

    printSeperator()
    fun main() {
        val files = listOf(File("/Z"), File("/a"))
        println(
            files.sortedWith(
                CaseInsensitiveFileComparator
            )
        )
    }
// [/a, /Z]

    val persons = listOf(Person("Bob"), Person("Alice"))
    println(persons.sortedWith(Person.NameComparator))
// [Person(name=Alice), Person(name=Bob)]
    printSeperator()
    MyClass.callMe()

}


class MyClass {
    companion object {
        fun callMe() {
            println("Companion object called")
        }
    }
}

data class Person(val name: String) {
    object NameComparator : Comparator<Person> {
        override fun compare(p1: Person, p2: Person): Int =
            p1.name.compareTo(p2.name)
    }
}


fun printSeperator(length: Int = 20) {
    println("*".repeat(length))
}

