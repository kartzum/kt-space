package aaa.abcd.sf.finit

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class IntTest {
    @Autowired
    lateinit var myClient: MyClient

    @Test
    fun test() {
        assert(myClient.calc().body == "ping_true")
    }
}