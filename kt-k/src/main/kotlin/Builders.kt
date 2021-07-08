import java.util.HashMap
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.SwingConstants

object FunctionLiteralsWithReceiver {
    fun task(): List<Boolean> {
        val isEven: Int.() -> Boolean = { this % 2 == 0 }
        val isOdd: Int.() -> Boolean = { this % 2 != 0 }

        return listOf(42.isOdd(), 239.isOdd(), 294823098.isEven())
    }

    fun run() {
        println(task())
    }
}

object StringAndMapBuilders {
    fun <K, V> buildMap(build: HashMap<K, V>.() -> Unit): Map<K, V> {
        val map = HashMap<K, V>()
        map.build()
        return map
    }

    fun usage(): Map<Int, String> {
        return buildMap {
            put(0, "0")
            for (i in 1..10) {
                put(i, "$i")
            }
        }
    }

    fun run() {
        println(usage())
    }
}

object TheFunctionApply {
    fun <T> T.myApply(f: T.() -> Unit): T {
        f()
        return this
    }

    fun createString(): String {
        return StringBuilder().myApply {
            append("Numbers: ")
            for (i in 1..10) {
                append(i)
            }
        }.toString()
    }

    fun createMap(): Map<Int, String> {
        return hashMapOf<Int, String>().myApply {
            put(0, "0")
            for (i in 1..10) {
                put(i, "$i")
            }
        }
    }

    fun run() {
        println(createString())
        println(createMap())
    }
}

object HTMLBuilder {
    data class Product(val description: String, val price: Double, val popularity: Int)

    val cactus = Product("cactus", 11.2, 13)
    val cake = Product("cake", 3.2, 111)
    val camera = Product("camera", 134.5, 2)
    val car = Product("car", 30000.0, 0)
    val carrot = Product("carrot", 1.34, 5)
    val cellPhone = Product("cell phone", 129.9, 99)
    val chimney = Product("chimney", 190.0, 2)
    val certificate = Product("certificate", 99.9, 1)
    val cigar = Product("cigar", 8.0, 51)
    val coffee = Product("coffee", 8.0, 67)
    val coffeeMaker = Product("coffee maker", 201.2, 1)
    val cola = Product("cola", 4.0, 67)
    val cranberry = Product("cranberry", 4.1, 39)
    val crocs = Product("crocs", 18.7, 10)
    val crocodile = Product("crocodile", 20000.2, 1)
    val cushion = Product("cushion", 131.0, 0)

    fun getProducts() = listOf(
        cactus, cake, camera, car, carrot, cellPhone, chimney, certificate, cigar, coffee, coffeeMaker,
        cola, cranberry, crocs, crocodile, cushion
    )


    open class Tag(val name: String) {
        val children = mutableListOf<Tag>()
        val attributes = mutableListOf<Attribute>()

        override fun toString(): String {
            return "<$name" +
                    (if (attributes.isEmpty()) "" else attributes.joinToString(separator = "", prefix = " ")) + ">" +
                    (if (children.isEmpty()) "" else children.joinToString(separator = "")) +
                    "</$name>"
        }
    }

    class Attribute(val name: String, val value: String) {
        override fun toString() = """$name="$value" """
    }

    fun <T : Tag> T.set(name: String, value: String?): T {
        if (value != null) {
            attributes.add(Attribute(name, value))
        }
        return this
    }

    fun <T : Tag> Tag.doInit(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    class Html : Tag("html")
    class Table : Tag("table")
    class Center : Tag("center")
    class TR : Tag("tr")
    class TD : Tag("td")
    class Text(val text: String) : Tag("b") {
        override fun toString() = text
    }

    fun html(init: Html.() -> Unit): Html = Html().apply(init)

    fun Html.table(init: Table.() -> Unit) = doInit(Table(), init)
    fun Html.center(init: Center.() -> Unit) = doInit(Center(), init)

    fun Table.tr(color: String? = null, init: TR.() -> Unit) = doInit(TR(), init).set("bgcolor", color)

    fun TR.td(color: String? = null, align: String = "left", init: TD.() -> Unit) =
        doInit(TD(), init).set("align", align).set("bgcolor", color)

    fun Tag.text(s: Any?) = doInit(Text(s.toString()), {})


    fun renderProductTable(): String {
        return html {
            table {
                tr(color = getTitleColor()) {
                    td {
                        text("Product")
                    }
                    td {
                        text("Price")
                    }
                    td {
                        text("Popularity")
                    }
                }
                val products = getProducts()
                for ((index, product) in products.withIndex()) {
                    tr {
                        td(color = getCellColor(index, 0)) {
                            text(product.description)
                        }
                        td(color = getCellColor(index, 1)) {
                            text(product.price)
                        }
                        td(color = getCellColor(index, 2)) {
                            text(product.popularity)
                        }
                    }
                }
            }
        }.toString()
    }

    fun getTitleColor() = "#b9c9fe"
    fun getCellColor(index: Int, row: Int) = if ((index + row) % 2 == 0) "#dce4ff" else "#eff2ff"


    //import javax.swing.JFrame
    //import javax.swing.JLabel
    //import javax.swing.JScrollPane
    //import javax.swing.SwingConstants.CENTER
    //
    //fun main() {
    //    with(JFrame("Product popularity")) {
    //        setSize(600, 600)
    //        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    //        add(JScrollPane(JLabel(renderProductTable(), SwingConstants.CENTER)))
    //        isVisible = true
    //    }
    //}


    fun run() {

    }
}

object BuildersHowTheyWork {
    enum class Answer { a, b, c }

    val answers = mapOf<Int, Answer?>(
        1 to Answer.c, 2 to Answer.b, 3 to Answer.b, 4 to Answer.c
    )

    fun run() {
        println(answers)
    }
}

object BuildersImplementation {
    open class Tag(val name: String) {
        protected val children = mutableListOf<Tag>()

        override fun toString() =
            "<$name>${children.joinToString("")}</$name>"
    }

    fun table(init: TABLE.() -> Unit): TABLE {
        val table = TABLE()
        table.init()
        return table
    }

    class TABLE : Tag("table") {
        fun tr(init: TR.() -> Unit) {
            val tr = TR()
            tr.init()
            children += tr
        }
    }

    class TR : Tag("tr") {
        fun td(init: TD.() -> Unit) {
            children += TD().apply(init)
        }
    }

    class TD : Tag("td")

    fun createTable() =
        table {
            tr {
                repeat(2) {
                    td {
                    }
                }
            }
        }

    fun run() {
        println(createTable())
    }
}

fun main() {
    FunctionLiteralsWithReceiver.run()
    StringAndMapBuilders.run()
    TheFunctionApply.run()
    HTMLBuilder.run()
    BuildersHowTheyWork.run()
    BuildersImplementation.run()
}