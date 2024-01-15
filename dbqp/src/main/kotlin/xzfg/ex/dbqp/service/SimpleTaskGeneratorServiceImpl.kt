package xzfg.ex.dbqp.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import xzfg.ex.dbqp.dbqueue.api.EnqueueParams
import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.dao.PostgresQueueDao
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Service
class SimpleTaskGeneratorServiceImpl : TaskGeneratorService {
    private val executor: ExecutorService = ThreadPoolExecutor(
        10,
        Int.MAX_VALUE,
        1L,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(),
    )

    @Autowired
    private lateinit var jdbcOperations: JdbcOperations

    override fun submit(queueTableSchema: QueueTableSchema, location: QueueLocation) {
        executor.submit(SimpleTaskExecutor(queueTableSchema, location, jdbcOperations))
    }

    override fun run(timeout: Long, unit: TimeUnit) {
        executor.awaitTermination(timeout, unit)
    }

    interface TaskExecutor : Runnable

    class SimpleTaskExecutor(
        queueTableSchema: QueueTableSchema,
        private val location: QueueLocation,
        jdbcOperations: JdbcOperations,
    ) : TaskExecutor {
        private val queueDao = PostgresQueueDao(
            jdbcTemplate = NamedParameterJdbcTemplate(jdbcOperations),
            queueTableSchema = queueTableSchema,
        )

        override fun run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    val id = UUID.randomUUID()
                    val data = "{\"id\":\"$id\"}"
                    val enqueueParams = EnqueueParams(payload = data)
                    queueDao.enqueue(location, enqueueParams)
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }
}
