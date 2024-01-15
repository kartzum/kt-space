package xzfg.ex.dbqp.service

import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.util.concurrent.TimeUnit

interface TaskExecutionService {
    fun submit(
        queueTableSchema: QueueTableSchema,
        location: QueueLocation,
        taskConsumer: TaskConsumer,
    )

    fun run(timeout: Long, unit: TimeUnit)
}
