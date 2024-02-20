package xzfg.ex.dbqp

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.util.StopWatch
import xzfg.ex.dbqp.dbqueue.api.TaskRecord
import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.dao.PostgresQueueDao
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import xzfg.ex.dbqp.service.TaskConsumer
import xzfg.ex.dbqp.service.TaskExecutionService
import xzfg.ex.dbqp.service.TaskGeneratorService
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@SpringBootTest
class DbqueueSpeedTests {
    @Autowired
    private lateinit var jdbcOperations: JdbcOperations

    @Autowired
    private lateinit var taskExecutionService: TaskExecutionService

    @Autowired
    private lateinit var taskGeneratorService: TaskGeneratorService

    private val tasksCounter: AtomicLong = AtomicLong()

    @Test
    @Disabled
    fun testSpeedHash() {
        val queueTableSchema = QueueTableSchema()
        val location = QueueLocation(tableName = "dev.queue_tasks_h")
        for (i in 0..2) {
            submitConsumer(queueTableSchema, location)
        }
        for (j in 0..1) {
            submitGenerator(queueTableSchema, location)
        }
        val n = 5
        val t = n * 60L
        val tg = ((n * 60) * 0.9).toLong()
        Watch(tasksCounter).run {
            taskExecutionService.run(t, TimeUnit.SECONDS)
            taskGeneratorService.run(tg, TimeUnit.SECONDS)
        }
        println("$t, $tg")
        // 65, 50.    tasks: 12523, seconds: 115, ts: 108
        // 120, 108.  tasks: 21974, seconds: 228, ts: 96
        // 300, 270.  tasks: 54444, seconds: 570, ts: 95
    }

    @Test
    @Disabled
    fun testSpeedHash8() {
        val queueTableSchema = QueueTableSchema()
        val location = QueueLocation(tableName = "dev.queue_tasks_h_8")
        for (i in 0..2) {
            submitConsumer(queueTableSchema, location)
        }
        for (j in 0..1) {
            submitGenerator(queueTableSchema, location)
        }
        val n = 10
        val t = n * 60L
        val tg = ((n * 60) * 0.9).toLong()
        Watch(tasksCounter).run {
            taskExecutionService.run(t, TimeUnit.SECONDS)
            taskGeneratorService.run(tg, TimeUnit.SECONDS)
        }
        println("$t, $tg")
        // 1.
        // 300, 270.     tasks: 60814, seconds: 570, ts: 106
        // 600, 540.     tasks: 110612, seconds: 1140, ts: 97
        // 900, 810.     tasks: 144771, seconds: 1710, ts: 84
        // 1800, 1620.   tasks: 279534, seconds: 3420, ts: 81
        // 2.
        // 300, 270.     tasks: 32611, seconds: 570, ts: 57
        // 600, 540.     tasks: 57500, seconds: 1140, ts: 50
    }

    @Test
    @Disabled
    fun testSpeedOriginal() {
        val queueTableSchema = QueueTableSchema()
        val location = QueueLocation(tableName = "dev.queue_tasks_o")
        for (i in 0..2) {
            submitConsumer(queueTableSchema, location)
        }
        for (j in 0..1) {
            submitGenerator(queueTableSchema, location)
        }
        val n = 30
        val t = n * 60L
        val tg = ((n * 60) * 0.9).toLong()
        Watch(tasksCounter).run {
            taskExecutionService.run(t, TimeUnit.SECONDS)
            taskGeneratorService.run(tg, TimeUnit.SECONDS)
        }
        println("$t, $tg")
        // 65, 50.     tasks: 13156, seconds: 115, ts: 114
        // 120, 108.   tasks: 19300, seconds: 228, ts: 84
        // 300, 270.   tasks: 61901, seconds: 570, ts: 108
        // 600, 540.   tasks: 64159, seconds: 1140, ts: 56
        // 600, 540.   tasks: 99629, seconds: 1140, ts: 87
        // 900, 810.   tasks: 182868, seconds: 1710, ts: 106
        // 1800, 1620. tasks: 301279, seconds: 3420, ts: 88
    }

    private fun submitConsumer(
        queueTableSchema: QueueTableSchema,
        location: QueueLocation,
    ) {
        taskExecutionService.submit(
            queueTableSchema = queueTableSchema,
            location = location,
            taskConsumer = StubTaskConsumer(
                jdbcOperations = jdbcOperations,
                queueTableSchema = queueTableSchema,
                location = location,
                tasksCounter = tasksCounter,
            ),
        )
    }

    private fun submitGenerator(
        queueTableSchema: QueueTableSchema,
        location: QueueLocation,
    ) {
        taskGeneratorService.submit(
            queueTableSchema = queueTableSchema,
            location = location,
        )
    }

    private class StubTaskConsumer(
        jdbcOperations: JdbcOperations,
        queueTableSchema: QueueTableSchema,
        private val location: QueueLocation,
        private val tasksCounter: AtomicLong,
    ) : TaskConsumer {
        private val queueDao = PostgresQueueDao(
            jdbcTemplate = NamedParameterJdbcTemplate(jdbcOperations),
            queueTableSchema = queueTableSchema,
        )

        override fun consume(tasks: List<TaskRecord>) {
            tasks.forEach {
                if (ThreadLocalRandom.current().nextInt(1000) < 5) {
                    println("${Thread.currentThread().id}, $it")
                }
                tasksCounter.incrementAndGet()
            }
            Thread.sleep(10)
            tasks.forEach { queueDao.deleteTask(location = location, taskId = it.id) }
        }
    }

    private class Watch(
        private val tasksCounter: AtomicLong
    ) {

        fun run(task: () -> Unit) {
            val stopWatch = StopWatch()
            stopWatch.start()
            task()
            stopWatch.stop()
            val tasks = tasksCounter.get()
            val seconds = stopWatch.totalTimeSeconds.toLong()
            val ts = tasks / seconds
            println("tasks: ${tasks}, seconds: ${seconds}, ts: $ts")
        }
    }
}
