package misc.kia_part_one.par1.ch4.section3

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import misc.kia_part_one.printSeperator
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

    override fun toString(): String {
        return "Person(name='$name', age=$age)"
    }
}

@Serializable
class ExtensionFunctionsPerson(val name: String, val age: Int) {


    companion object {
        fun test(): Unit {}
    }

    override fun toString(): String {
        return "ExtensionFunctionsPerson(name='$name', age=$age)"
    }
}

fun ExtensionFunctionsPerson.Companion.fromJSON(jsonText: String): ExtensionFunctionsPerson {
    return Json.decodeFromString<ExtensionFunctionsPerson>(jsonText)
}


@Serializable
class SerialPerson(var name: String, var age: Int) {
    companion object Loader : JSONFactory<SerialPerson> {
        override fun fromJSON(jsonText: String): SerialPerson {
            return Json.decodeFromString<SerialPerson>(jsonText)
        }
    }

    override fun toString(): String {
        return "SerialPerson(name='$name', age=$age)"
    }


}


fun main() {
    val jsonString = """{"name": "Bob", "age": 25}"""

    printSeperator()
    val serialPerson = SerialPerson.fromJSON(jsonString)
    println(serialPerson)
    printSeperator()

    printSeperator()
    val r1 = Random.Default.nextInt(0, 100).coerceIn(18..35)
    println("r1=$r1")
    val r2 = Random.nextInt()
    printSeperator()

    printSeperator()
    val person = Person.fromJSON(jsonString)
    println(person)
    printSeperator()

    printSeperator()
    val personExtensionFunctions = ExtensionFunctionsPerson.fromJSON(jsonString)
    println(personExtensionFunctions)
    printSeperator()
}
