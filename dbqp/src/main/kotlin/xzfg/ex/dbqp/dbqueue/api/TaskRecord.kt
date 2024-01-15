package xzfg.ex.dbqp.dbqueue.api

import java.time.ZonedDateTime

data class TaskRecord(
    val id: Long,
    val payload: String,
    val attemptsCount: Long,
    val reenqueueAttemptsCount: Long,
    val totalAttemptsCount: Long,
    val createdAt: ZonedDateTime,
    val nextProcessAt: ZonedDateTime,
    val extData: MutableMap<String, String> = HashMap(),
)
