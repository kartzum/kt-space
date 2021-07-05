import kotlin.random.Random as KRandom
import java.util.Random as JRandom

object DataClasses {
    data class Person(val name: String, val age: Int)

    private fun getPeople(): List<Person> {
        return listOf(Person("Alice", 29), Person("Bob", 31))
    }

    private fun comparePeople(): Boolean {
        val p1 = Person("Alice", 29)
        val p2 = Person("Alice", 29)
        return p1 == p2  // should be true
    }

    fun run() {
        println(getPeople())
        println(comparePeople())
    }
}

object SmartCasts {
    private fun eval(expr: Expr): Int =
        when (expr) {
            is Num -> expr.value
            is Sum -> eval(expr.left) + eval(expr.right)
            else -> throw IllegalArgumentException("Unknown expression")
        }

    interface Expr
    class Num(val value: Int) : Expr
    class Sum(val left: Expr, val right: Expr) : Expr

    fun run() {
        println(eval(Num(42)))
        println(eval(Sum(Num(3), Num(2))))
    }
}

object SealedClasses {
    /*fun eval(expr: Expr): Int =
        when (expr) {
            is Num -> expr.value
            is Sum -> eval(expr.left) + eval(expr.right)
        }

    sealed class Expr
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()*/

    fun run() {

    }
}

object RenameOnImport {
    private fun useDifferentRandomClasses(): String {
        return "Kotlin random: " +
                KRandom.nextInt(2) +
                " Java random:" +
                JRandom().nextInt(2) +
                "."
    }

    fun run() {
        println(useDifferentRandomClasses())
    }
}

object ExtensionFunctions {
    fun Int.r(): RationalNumber = RationalNumber(this, 1)

    fun Pair<Int, Int>.r(): RationalNumber = RationalNumber(first, second)

    data class RationalNumber(val numerator: Int, val denominator: Int)

    fun run() {

    }
}

fun main() {
    DataClasses.run()
    SmartCasts.run()
    SealedClasses.run()
    RenameOnImport.run()
    ExtensionFunctions.run()
}