package xzfg.ex.dbqp.service

import xzfg.ex.dbqp.dbqueue.config.QueueTableSchema
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.util.concurrent.TimeUnit

interface TaskGeneratorService {
    fun submit(
        queueTableSchema: QueueTableSchema,
        location: QueueLocation,
    )

    fun run(timeout: Long, unit: TimeUnit)
}
