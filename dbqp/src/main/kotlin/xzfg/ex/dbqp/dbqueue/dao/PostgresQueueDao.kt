package xzfg.ex.dbqp.dbqueue.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import xzfg.ex.dbqp.dbqueue.api.EnqueueParams
import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.time.Duration
import java.util.stream.Collectors

class PostgresQueueDao(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val queueTableSchema: QueueTableSchema,
) : QueueDao {

    override fun enqueue(location: QueueLocation, enqueueParams: EnqueueParams<String>): Long {
        val params = MapSqlParameterSource()
            .addValue("queueName", location.queueId.id)
            .addValue("payload", enqueueParams.payload)
            .addValue("executionDelay", enqueueParams.executionDelay.getSeconds())

        queueTableSchema.extFields.forEach { paramName -> params.addValue(paramName, null) }
        enqueueParams.extData.forEach(params::addValue)
        return jdbcTemplate.queryForObject(createEnqueueSql(location), params, Long::class.java)!!
    }

    override fun deleteTask(location: QueueLocation, taskId: Long): Boolean {
        val params = MapSqlParameterSource()
            .addValue("id", taskId)
            .addValue("queueName", location.queueId.id)
        return jdbcTemplate.update(createDeleteSql(location), params) != 0
    }

    override fun reenqueue(location: QueueLocation, taskId: Long, executionDelay: Duration): Boolean {
        val params = MapSqlParameterSource()
            .addValue("id", taskId)
            .addValue("queueName", location.queueId.id)
            .addValue("executionDelay", executionDelay.getSeconds())
        return jdbcTemplate.update(createReenqueueSql(location), params) != 0
    }

    private fun createEnqueueSql(location: QueueLocation): String {
        return "INSERT INTO " + location.tableName + "(" +
            location.getIdSequence().map { ignored -> queueTableSchema.idField + "," }.orElse("") +
            queueTableSchema.queueNameField + "," +
            queueTableSchema.payloadField + "," +
            queueTableSchema.nextProcessAtField + "," +
            queueTableSchema.reenqueueAttemptField + "," +
            queueTableSchema.totalAttemptField +
            (if (queueTableSchema.extFields.isEmpty()) "" else queueTableSchema.extFields.stream()
                .collect(Collectors.joining(", ", ", ", ""))) +
            ") VALUES " +
            "(" + location.getIdSequence().map { seq -> "nextval('$seq'), " }.orElse("") +
            ":queueName, :payload, now() + :executionDelay * INTERVAL '1 SECOND', 0, 0" +
            (if (queueTableSchema.extFields.isEmpty()) "" else queueTableSchema.extFields.stream()
                .map { field -> ":$field" }.collect(Collectors.joining(", ", ", ", ""))) +
            ") RETURNING " + queueTableSchema.idField;
    }

    private fun createDeleteSql(location: QueueLocation): String {
        return "DELETE FROM " + location.tableName + " WHERE " + queueTableSchema.queueNameField +
            " = :queueName AND " + queueTableSchema.idField + " = :id";
    }

    private fun createReenqueueSql(location: QueueLocation): String {
        return "UPDATE " + location.tableName + " SET " + queueTableSchema.nextProcessAtField +
            " = now() + :executionDelay * INTERVAL '1 SECOND', " +
            queueTableSchema.attemptField + " = 0, " +
            queueTableSchema.reenqueueAttemptField +
            " = " + queueTableSchema.reenqueueAttemptField + " + 1 " +
            "WHERE " + queueTableSchema.idField + " = :id AND " +
            queueTableSchema.queueNameField + " = :queueName"
    }
}
