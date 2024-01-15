package xzfg.ex.dbqp.dbqueue.config

import kotlin.collections.ArrayList

data class QueueTableSchema(
    val idField: String = "id",
    val queueNameField: String = "queue_name",
    val payloadField: String = "payload",
    val attemptField: String = "attempt",
    val reenqueueAttemptField: String = "reenqueue_attempt",
    val totalAttemptField: String = "total_attempt",
    val createdAtField: String = "created_at",
    val nextProcessAtField: String = "next_process_at",
    val extFields: ArrayList<String> = java.util.ArrayList(),
)
