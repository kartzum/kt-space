package xzfg.ex.dbqp

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
class JdbcTests {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var jdbcOperations: JdbcOperations

    @Test
    @Disabled
    fun testConnectionWithJdbc() {
        jdbcTemplate.query(
            "select id from lom_order limit 1",
        ) { i ->
            println(i.getLong(1))
        }
    }

    @Test
    @Disabled
    fun testConnectionWithJdbcOperations() {
        jdbcOperations.query("select id from lom_order limit 1") { i ->
            println(i.getLong(1))
        }
    }
}
