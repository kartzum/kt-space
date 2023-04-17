package kr1.abcd.aaa

import io.ktor.server.application.*
import kr1.abcd.aaa.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSerialization()
}
