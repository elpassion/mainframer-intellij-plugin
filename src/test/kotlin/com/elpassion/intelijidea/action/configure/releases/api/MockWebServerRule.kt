package com.elpassion.intelijidea.action.configure.releases.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import retrofit2.Retrofit

class MockWebServerRule : TestRule {

    val webServer = MockWebServer()
    val retrofit: Retrofit = provideBaseRetrofitBuilder().baseUrl(webServer.url("/")).build()

    override fun apply(statement: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    statement.evaluate()
                } finally {
                    webServer.shutdown()
                }
            }
        }
    }

    fun stubWebToReturn(body: String) {
        webServer.enqueue(MockResponse().setBody(body))
    }
}