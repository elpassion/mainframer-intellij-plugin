package action

import com.elpassion.intelijidea.action.configure.releases.api.provideBaseRetrofitBuilder
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class GithubApiIntegrationTest() {
    val webserver = MockWebServer()

    @After
    fun tearDown() {
        webserver.shutdown()
    }

    @Test
    fun shouldReturnReleaseWithTagName() {
        webserver.enqueue(MockResponse().setBody("[{\"tag_name\": \"v1.1.2\" }]"))

        val listReleases = callRequestForReleases()
        assertEquals("v1.1.2", listReleases.first().tagName)
    }

    @Test
    fun shouldReturnTwoReleases() {
        webserver.enqueue(MockResponse().setBody("[{\"tag_name\": \"v1.1.2\" }, {\"tag_name\": \"v1.1.0\" }]"))

        val listReleases = callRequestForReleases()
        assertEquals(2, listReleases.size)
    }

    private fun callRequestForReleases() = provideGithubApi(provideTestRetrofit()).listReleases().blockingFirst()

    private fun provideTestRetrofit() = provideBaseRetrofitBuilder().baseUrl(webserver.url("/")).build()
}
