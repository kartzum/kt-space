import java.lang.IllegalArgumentException

object HW {
    private fun start(): String = "OK"

    fun run() {
        println(start())
    }
}

object NamedArgs {
    private fun joinOptions(options: Collection<String>) =
        options.joinToString(prefix = "[", postfix = "]")


    fun run() {
        println(joinOptions(listOf("1", "2", "3")))
    }
}

object DefaultArguments {
    private fun foo(name: String = "a", number: Int = 42, toUpperCase: Boolean = false) =
        (if (toUpperCase) name.toUpperCase() else name) + number

    private fun useFoo() = listOf(
        foo("a"),
        foo("b", number = 1),
        foo("c", toUpperCase = true),
        foo(name = "d", number = 2, toUpperCase = true)
    )

    fun run() {
        println(useFoo())
    }
}

object TripleQuotedString {
    private const val question = "life, the universe, and everything"
    private const val answer = 42

    private val tripleQuotedString = """
    #question = "$question"
    #answer = $answer""".trimMargin(marginPrefix = "#")

    fun run() {
        println(tripleQuotedString)
    }
}

object StringTemplates {
    private const val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)"

    private fun getPattern(): String = """\d{2} $month \d{4}"""

    fun run() {
        println(getPattern())
    }
}

object NullableTypes {
    private fun sendMessageToClient(
        client: Client?, message: String?, mailer: Mailer
    ) {
        val email = client?.personalInfo?.email
        if (email != null && message != null) {
            mailer.sendMessage(email, message)
        }
    }

    class Client(val personalInfo: PersonalInfo?)
    class PersonalInfo(val email: String?)
    interface Mailer {
        fun sendMessage(email: String, message: String)
    }

    class MailerImpl : Mailer {
        override fun sendMessage(email: String, message: String) {
            println("""$email, $message""")
        }
    }

    fun run() {
        sendMessageToClient(Client(PersonalInfo("email")), "message", MailerImpl())
    }
}

object NothingType {
    private fun failWithWrongAge(age: Int?): Nothing {
        throw IllegalArgumentException("Wrong age: $age")
    }

    private fun checkAge(age: Int?) {
        if (age == null || age !in 0..150) failWithWrongAge(age)
        println("Congrats! Next year you'll be ${age + 1}.")
    }

    fun run() {
        checkAge(10)
    }
}

object Lambdas {
    private fun containsEven(collection: Collection<Int>): Boolean =
        collection.any { it % 2 == 0 }

    fun run() {
        println(containsEven(listOf(1, 2, 3, 4, 5)))
    }
}

fun main() {
    HW.run()
    NamedArgs.run()
    DefaultArguments.run()
    TripleQuotedString.run()
    StringTemplates.run()
    NullableTypes.run()
    NothingType.run()
    Lambdas.run()
}