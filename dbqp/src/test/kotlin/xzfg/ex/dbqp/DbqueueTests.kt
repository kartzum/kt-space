package xzfg.ex.dbqp

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import xzfg.ex.dbqp.dbqueue.api.EnqueueParams
import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.dao.PostgresQueueDao
import xzfg.ex.dbqp.dbqueue.dao.PostgresQueuePickTaskDao
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.time.Duration

@SpringBootTest
class DbqueueTests {
    @Autowired
    private lateinit var jdbcOperations: JdbcOperations

    @Test
    @Disabled
    fun testOperationsOnTask() {
        val queueDao = PostgresQueueDao(
            jdbcTemplate = NamedParameterJdbcTemplate(jdbcOperations),
            queueTableSchema = QueueTableSchema(),
        )
        val location = QueueLocation()
        val enqueueParams = EnqueueParams(payload = "")

        val id = queueDao.enqueue(location, enqueueParams)

        queueDao.reenqueue(location, id, Duration.ofSeconds(42))

        queueDao.deleteTask(location, id)
    }

    @Test
    @Disabled
    fun testPickTasks() {
        val queuePickTaskDao = PostgresQueuePickTaskDao(
            jdbcTemplate = NamedParameterJdbcTemplate(jdbcOperations),
            queueTableSchema = QueueTableSchema(),
            location = QueueLocation(),
        )

        queuePickTaskDao.pickTasksBatch().forEach {
            println(it)
        }
    }
}
