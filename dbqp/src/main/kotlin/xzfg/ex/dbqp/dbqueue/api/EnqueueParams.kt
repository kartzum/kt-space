package xzfg.ex.dbqp.dbqueue.api

import java.time.Duration

class EnqueueParams<T>(
    val payload: T,
    val executionDelay: Duration = Duration.ZERO,
    val extData: Map<String, String> = LinkedHashMap(),
)
