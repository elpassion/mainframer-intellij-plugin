package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.ReleaseApiModel
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class MFReleasesFetcherTest {
    val api = mock<GitHubApi>()
    val releasesFetcher = mfReleasesFetcher(api)

    @Test
    fun shouldDropFirstCharacter() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"))

        releasesFetcher()
                .test()
                .assertValue{ it.first() == "1.1.2"}
    }

    @Test
    fun shouldProperMapReleaseApiModelToString() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"), ReleaseApiModel("v1.1.3"))

        releasesFetcher()
                .test()
                .assertValue{ it[0] == "1.1.2"}
                .assertValue{ it[1] == "1.1.3"}
    }

    private fun stubApiToReturn(vararg response: ReleaseApiModel) =
            whenever(api.listReleases()).thenJust(response.toList())
}
