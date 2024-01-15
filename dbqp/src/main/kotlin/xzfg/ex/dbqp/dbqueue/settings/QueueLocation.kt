package xzfg.ex.dbqp.dbqueue.settings

import java.util.Optional

data class QueueLocation(
    val tableName: String = "dev.queue_tasks_h",
    val queueId: QueueId = QueueId("create_order"),
    val idSequence: String? = null,
) {
    fun getIdSequence(): Optional<String> = Optional.ofNullable(idSequence)
}
