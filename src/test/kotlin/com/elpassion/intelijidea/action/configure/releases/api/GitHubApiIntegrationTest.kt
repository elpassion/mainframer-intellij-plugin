package com.elpassion.intelijidea.action.configure.releases.api

import org.junit.Rule
import org.junit.Test

class GitHubApiIntegrationTest {

    @JvmField @Rule
    val rule = MockWebServerRule()

    @Test
    fun shouldReturnReleaseWithTagName() {
        rule.stubWebToReturn("[{\"tag_name\": \"v1.1.2\" }]")

        callRequestForReleases()
                .test()
                .assertValue { it.first().tagName == "v1.1.2" }
    }

    @Test
    fun shouldReturnTwoReleases() {
        rule.stubWebToReturn("[{\"tag_name\": \"v1.1.2\" }, {\"tag_name\": \"v1.1.0\" }]")

        callRequestForReleases()
                .test()
                .assertValue { it.size == 2 }
    }

    private fun callRequestForReleases() = provideGitHubApi(rule.retrofit).listReleases()
}
