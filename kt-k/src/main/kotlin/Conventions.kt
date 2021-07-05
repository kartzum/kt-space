import java.util.Calendar

object Comparison {
    data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
        override fun compareTo(other: MyDate) = when {
            year != other.year -> year - other.year
            month != other.month -> month - other.month
            else -> dayOfMonth - other.dayOfMonth
        }
    }

    fun test(date1: MyDate, date2: MyDate) {
        // this code should compile:
        println(date1 < date2)
    }


    fun run() {
        test(MyDate(2001, 1, 1), MyDate(2001, 1, 3))
    }
}

object Ranges {
    data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
        override fun compareTo(other: MyDate): Int {
            if (year != other.year) return year - other.year
            if (month != other.month) return month - other.month
            return dayOfMonth - other.dayOfMonth
        }
    }

    fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
        return date in first..last
    }

    fun run() {
        println(checkInRange(MyDate(2001, 1, 2), MyDate(2001, 1, 1), MyDate(2001, 1, 3)))
    }
}

object ForLoop {
    /*
     * Returns the following date after the given one.
     * For example, for Dec 31, 2019 the date Jan 1, 2020 is returned.
     */
    fun MyDate.followingDate(): MyDate {
        val c = Calendar.getInstance()
        c.set(year, month, dayOfMonth)
        val millisecondsInADay = 24 * 60 * 60 * 1000L
        val timeInMillis = c.timeInMillis + millisecondsInADay
        val result = Calendar.getInstance()
        result.timeInMillis = timeInMillis
        return MyDate(result.get(Calendar.YEAR), result.get(Calendar.MONTH), result.get(Calendar.DATE))
    }

    data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
        override fun compareTo(other: MyDate): Int {
            if (year != other.year) return year - other.year
            if (month != other.month) return month - other.month
            return dayOfMonth - other.dayOfMonth
        }
    }

    operator fun MyDate.rangeTo(other: MyDate) = DateRange(this, other)

    class DateRange(val start: MyDate, val end: MyDate) : Iterable<MyDate> {
        override fun iterator(): Iterator<MyDate> {
            return object : Iterator<MyDate> {
                var current: MyDate = start

                override fun next(): MyDate {
                    if (!hasNext()) throw NoSuchElementException()
                    val result = current
                    current = current.followingDate()
                    return result
                }

                override fun hasNext(): Boolean = current <= end
            }
        }
    }

    fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
        for (date in firstDate..secondDate) {
            handler(date)
        }
    }

    fun run() {
        iterateOverDateRange(MyDate(2001, 1, 1), MyDate(2001, 1, 3)) { it -> println(it) }
    }
}

object OperatorsOverloading {
    /*
     * Returns the date after the given time interval.
     * The interval is specified as the given amount of days, weeks of years.
     * Usages:
     * 'date.addTimeIntervals(TimeInterval.DAY, 4)'
     * 'date.addTimeIntervals(TimeInterval.WEEK, 3)'
     */
    fun MyDate.addTimeIntervals(timeInterval: TimeInterval, amount: Int): MyDate {
        val c = Calendar.getInstance()
        c.set(year + if (timeInterval == TimeInterval.YEAR) amount else 0, month, dayOfMonth)
        var timeInMillis = c.timeInMillis
        val millisecondsInADay = 24 * 60 * 60 * 1000L
        timeInMillis += amount * when (timeInterval) {
            TimeInterval.DAY -> millisecondsInADay
            TimeInterval.WEEK -> 7 * millisecondsInADay
            TimeInterval.YEAR -> 0L
        }
        val result = Calendar.getInstance()
        result.timeInMillis = timeInMillis
        return MyDate(result.get(Calendar.YEAR), result.get(Calendar.MONTH), result.get(Calendar.DATE))
    }

    // import TimeInterval.*

    data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int)

    // Supported intervals that might be added to dates:
    enum class TimeInterval { DAY, WEEK, YEAR }

    operator fun MyDate.plus(timeInterval: TimeInterval): MyDate = addTimeIntervals(timeInterval, 1)

    class RepeatedTimeInterval(val timeInterval: TimeInterval, val number: Int)

    operator fun TimeInterval.times(number: Int) =
        RepeatedTimeInterval(this, number)

    operator fun MyDate.plus(timeIntervals: RepeatedTimeInterval) =
        addTimeIntervals(timeIntervals.timeInterval, timeIntervals.number)

    fun task1(today: MyDate): MyDate {
        return today + TimeInterval.YEAR + TimeInterval.WEEK
    }

    fun task2(today: MyDate): MyDate {
        return today + TimeInterval.YEAR * 2 + TimeInterval.WEEK * 3 + TimeInterval.DAY * 5
    }

    fun run() {
        println(task1(MyDate(2001, 1, 1)))
        println(task2(MyDate(2001, 1, 1)))
    }
}

object Invoke {
    class Invokable {
        var numberOfInvocations: Int = 0
            private set

        operator fun invoke(): Invokable {
            numberOfInvocations++
            return this
        }
    }

    fun invokeTwice(invokable: Invokable) = invokable()()

    fun run() {
        val i = Invokable()
        invokeTwice(i)
        println(i.numberOfInvocations)
    }
}

fun main() {
    Comparison.run()
    Ranges.run()
    ForLoop.run()
    OperatorsOverloading.run()
    Invoke.run()
}