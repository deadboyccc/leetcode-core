package misc.kia_part_one.par1.ch4.section3

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

@Serializable
class Person(val name: String, val age: Int) {
    companion object Loader : JSONFactory<Person> {

        override fun fromJSON(jsonText: String): Person {
            return Json.decodeFromString<Person>(jsonText)
        }
    }
}


fun main() {
    val r1 = Random.Default.nextInt()
    val r2 = Random.nextInt()

    val person = Person.fromJSON("""{"name": "Bob", "age": 25}""")
}
