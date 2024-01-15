package xzfg.ex.dbqp.service

import xzfg.ex.dbqp.dbqueue.api.TaskRecord

interface TaskConsumer {
    fun consume(tasks: List<TaskRecord>)
}
