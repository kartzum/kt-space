package xzfg.ex.dbqp.dbqueue.dao

import xzfg.ex.dbqp.dbqueue.api.EnqueueParams
import xzfg.ex.dbqp.dbqueue.settings.QueueLocation
import java.time.Duration

interface QueueDao {
    fun enqueue(location: QueueLocation, enqueueParams: EnqueueParams<String>): Long

    fun deleteTask(location: QueueLocation, taskId: Long): Boolean

    fun reenqueue(location: QueueLocation, taskId: Long, executionDelay: Duration): Boolean
}
