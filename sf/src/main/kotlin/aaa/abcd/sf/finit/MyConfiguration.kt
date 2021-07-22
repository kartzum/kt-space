package aaa.abcd.sf.finit

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MyConfiguration {
    @Autowired
    lateinit var httpTemplate: HttpTemplateService

    @Bean
    fun myClient() = MyClientImpl(httpTemplate, true)
}