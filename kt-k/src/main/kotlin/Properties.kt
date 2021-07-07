import java.util.Calendar
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Properties {
    class PropertyExample() {
        var counter = 0
        var propertyWithCounter: Int? = null
            set(v) {
                field = v
                counter++
            }
    }

    fun run() {
        val propertyExample = PropertyExample()
        println("${propertyExample.propertyWithCounter}, ${propertyExample.counter}")
        propertyExample.propertyWithCounter = 1;
        println("${propertyExample.propertyWithCounter}, ${propertyExample.counter}")
    }
}

object LazyProperty {
    class LazyProperty(val initializer: () -> Int) {
        var value: Int? = null
        val lazy: Int
            get() {
                if (value == null) {
                    value = initializer()
                }
                return value!!
            }
    }

    fun run() {
        val lazyProperty = LazyProperty { 10 }
        println(lazyProperty.value)
        println(lazyProperty.lazy)
    }
}

object DelegatesExample {
    class LazyProperty(val initializer: () -> Int) {
        val lazyValue: Int by lazy(initializer)
    }

    fun run() {
        val lazyProperty = LazyProperty { 10 }
        println(lazyProperty.lazyValue)
    }
}

object DelegatesHowItWorks {
    data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int)

    fun MyDate.toMillis(): Long {
        val c = Calendar.getInstance()
        c.set(year, month, dayOfMonth)
        return c.getTimeInMillis()
    }

    fun Long.toDate(): MyDate {
        val c = Calendar.getInstance()
        c.setTimeInMillis(this)
        return MyDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE))
    }

    class D {
        var date: MyDate by EffectiveDate()
    }

    class EffectiveDate<R> : ReadWriteProperty<R, MyDate> {

        var timeInMillis: Long? = null

        override fun getValue(thisRef: R, property: KProperty<*>): MyDate {
            return timeInMillis!!.toDate()
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: MyDate) {
            timeInMillis = value.toMillis()
        }
    }

    fun run() {
        val d = D()
        d.date = MyDate(2001, 10, 1)
        println(d.date)
    }
}

fun main() {
    Properties.run()
    LazyProperty.run()
    DelegatesExample.run()
    DelegatesHowItWorks.run()
}