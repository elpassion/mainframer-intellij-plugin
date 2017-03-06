package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.ReleaseApiModel
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test

class MFReleasesFetcherTest {
    val api = mock<GitHubApi>()
    val releasesFetcher = mfReleasesFetcher(api)

    @Test
    fun shouldDropFirstCharacter() {
        stubApiToReturn(ReleaseApiModel("v2.1.2"))

        releasesFetcher()
                .test()
                .assertValue { it.first() == "2.1.2" }
    }

    @Test
    fun shouldProperMapReleaseApiModelToString() {
        stubApiToReturn(ReleaseApiModel("v2.1.2"), ReleaseApiModel("v2.1.3"))

        releasesFetcher()
                .test()
                .assertValue { it == listOf("2.1.2", "2.1.3") }
    }

    @Test
    fun shouldNotReturnAnyVariantsOfVersionOne() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"), ReleaseApiModel("v2.0.0"))

        releasesFetcher()
                .test()
                .assertValue { it.first() == "2.0.0" }
                .assertValueCount(1)
    }

    private fun stubApiToReturn(vararg response: ReleaseApiModel) =
            whenever(api.listReleases()).thenJust(response.toList())
}
