package xzfg.ex.dbqp.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.dao.PostgresQueuePickTaskDao
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Service
class TaskExecutionServiceImpl : TaskExecutionService {
    private val executor: ExecutorService = ThreadPoolExecutor(
        10,
        Int.MAX_VALUE,
        1L,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(),
    )

    @Autowired
    private lateinit var jdbcOperations: JdbcOperations

    override fun submit(
        queueTableSchema: QueueTableSchema,
        location: QueueLocation,
        taskConsumer: TaskConsumer,
    ) {
        executor.submit(SimpleTaskExecutor(queueTableSchema, location, taskConsumer, jdbcOperations))
    }

    override fun run(timeout: Long, unit: TimeUnit) {
        executor.awaitTermination(timeout, unit)
    }

    interface TaskExecutor : Runnable

    class SimpleTaskExecutor(
        queueTableSchema: QueueTableSchema,
        location: QueueLocation,
        private val taskConsumer: TaskConsumer,
        jdbcOperations: JdbcOperations,
    ) : TaskExecutor {

        private val queuePickTaskDao = PostgresQueuePickTaskDao(
            jdbcTemplate = NamedParameterJdbcTemplate(jdbcOperations),
            queueTableSchema = queueTableSchema,
            location = location,
        )

        override fun run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    val tasks = queuePickTaskDao.pickTasksBatch()
                    taskConsumer.consume(tasks)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }
}
