package xzfg.ex.dbqp.dbqueue.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import xzfg.ex.dbqp.dbqueue.api.TaskRecord
import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.stream.Collectors

class PostgresQueuePickTaskDao(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val queueTableSchema: QueueTableSchema,
    location: QueueLocation,
) : QueuePickTaskDao {

    private val pickTasksSql = createPickTasksSql(location)
    private val pickTaskSqlPlaceholders = MapSqlParameterSource()
        .addValue("queueName", location.queueId.id)
        .addValue("retryInterval", 0)
        .addValue("batchSize", 10)

    override fun pickTasksBatch(): List<TaskRecord> {
        return jdbcTemplate.execute(
            pickTasksSql,
            pickTaskSqlPlaceholders
        ) { ps: PreparedStatement ->
            ps.executeQuery().use { rs ->
                val taskRecords = ArrayList<TaskRecord>()
                while (rs.next()) {
                    taskRecords.add(createTaskRecord(rs))
                }
                taskRecords
            }
        } ?: listOf()
    }

    private fun createTaskRecord(rs: ResultSet): TaskRecord {
        val additionalData = LinkedHashMap<String, String>()
        queueTableSchema.extFields.forEach { key ->
            try {
                additionalData.put(key, rs.getString(key))
            } catch (e: SQLException) {
                throw RuntimeException(e)
            }
        }
        return TaskRecord(
            id = rs.getLong(queueTableSchema.idField),
            createdAt = getZonedDateTime(rs, queueTableSchema.createdAtField),
            nextProcessAt = getZonedDateTime(rs, queueTableSchema.nextProcessAtField),
            payload = rs.getString(queueTableSchema.payloadField),
            attemptsCount = rs.getLong(queueTableSchema.attemptField),
            reenqueueAttemptsCount = rs.getLong(queueTableSchema.reenqueueAttemptField),
            totalAttemptsCount = rs.getLong(queueTableSchema.reenqueueAttemptField),
            extData = additionalData,
        )
    }

    @Throws(SQLException::class)
    private fun getZonedDateTime(rs: ResultSet, time: String): ZonedDateTime {
        return ZonedDateTime.ofInstant(rs.getTimestamp(time).toInstant(), ZoneId.systemDefault())
    }

    private fun createPickTasksSql(location: QueueLocation): String {
        val ext = if (queueTableSchema.extFields.isEmpty()) {
            ""
        } else {
            queueTableSchema.extFields.stream().map { field -> "q." + field }
                .collect(Collectors.joining(", ", ", ", ""))
        }
        return "WITH cte AS (" +
            "SELECT " + queueTableSchema.idField + " " +
            "FROM " + location.tableName + " " +
            "WHERE " + queueTableSchema.queueNameField + " = :queueName " +
            "  AND " + queueTableSchema.nextProcessAtField + " <= now() " +
            " ORDER BY " + queueTableSchema.nextProcessAtField + " ASC " +
            "LIMIT :batchSize " +
            "FOR UPDATE SKIP LOCKED) " +
            "UPDATE " + location.tableName + " q " +
            "SET " +
            "  " + queueTableSchema.nextProcessAtField + " = " +
            getNextProcessTimeSql() + ", " +
            "  " + queueTableSchema.attemptField + " = " + queueTableSchema.attemptField + " + 1, " +
            "  " + queueTableSchema.totalAttemptField + " = " +
            queueTableSchema.totalAttemptField + " " + "+ 1 " +
            "FROM cte " +
            "WHERE q." + queueTableSchema.idField + " = cte." + queueTableSchema.idField + " " +
            "RETURNING q." + queueTableSchema.idField + ", " +
            "q." + queueTableSchema.payloadField + ", " +
            "q." + queueTableSchema.attemptField + ", " +
            "q." + queueTableSchema.reenqueueAttemptField + ", " +
            "q." + queueTableSchema.totalAttemptField + ", " +
            "q." + queueTableSchema.createdAtField + ", " +
            "q." + queueTableSchema.nextProcessAtField +
            ext
    }

    private fun getNextProcessTimeSql(): String =
        "now() + (1 + (" + queueTableSchema.attemptField + " * 2)) * :retryInterval * INTERVAL '1" +
            " SECOND'";
}
