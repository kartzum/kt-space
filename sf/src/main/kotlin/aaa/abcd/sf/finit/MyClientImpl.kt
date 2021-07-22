package aaa.abcd.sf.finit

class MyClientImpl(
    private var httpTemplate: HttpTemplateService,
    private var flag: Boolean
) : MyClient {

    override fun calc(): MyResponse {
        return MyResponse("${httpTemplate.ping()}_$flag")
    }
}