package aaa.abcd.sf.finit

interface MyClient {
    fun calc(): MyResponse
}

data class MyResponse(val body: String)