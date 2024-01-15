package xzfg.ex.dbqp.dbqueue.dao

import xzfg.ex.dbqp.dbqueue.api.TaskRecord

interface QueuePickTaskDao {
    fun pickTasksBatch(): List<TaskRecord>
}
