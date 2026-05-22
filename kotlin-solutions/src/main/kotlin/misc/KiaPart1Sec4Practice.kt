package misc.kia_part_one.practice.sec4

interface MouseListener {
    fun onEnter()
    fun onClick()
}

class Button(private val listener: MouseListener) { /* ...
*/
}

fun main() {
    val listener = object : MouseListener {
        override fun onEnter() { /* ... */
        }

        override fun onClick() { /* ... */
        }
    }
    val button = Button(object : MouseListener {
        override fun onEnter() {}
        override fun onClick() {}
    })

    val expense = UsdCent(1_99)
    println(expense.salesTax)
    // 11.94
    expense.prettyPrint()
    // 199¢
    val people = listOf(
        Person("Alice", 23),
        Person("Bob", 28),
        Person("Carol", 30),
        Person("Dave", 28),
        Person("Carol", 90),
        Person("Dave", 10),
    )
    val eldestPerson = people.maxByOrNull(Person::age)
    println(eldestPerson?.age)


}

data class Person(val name: String, val age: Int)
interface PrettyPrintable {
    fun prettyPrint()
}

@JvmInline
value class UsdCent(val amount: Int) : PrettyPrintable {
    val salesTax
        get() = amount * 0.06

    override fun prettyPrint() = println("${amount}¢")
}

fun demo(): Unit {
    var clickCount = 0
    Button(object : MouseListener {
        override fun onEnter() { /* ... */
        }

        override fun onClick() {
            clickCount++
        }
    })
}

