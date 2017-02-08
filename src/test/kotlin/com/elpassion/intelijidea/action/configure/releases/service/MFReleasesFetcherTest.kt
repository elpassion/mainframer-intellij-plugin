package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.ReleaseApiModel
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test

class MFReleasesFetcherTest {
    val api = mock<GitHubApi>()
    val service = MFReleasesFetcher(api)

    @Test
    fun shouldDropFirstCharacter() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"))

        service.getVersions()
                .test()
                .assertValue{ it.first() == "1.1.2"}
    }

    @Test
    fun shouldProperMapReleaseApiModelToString() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"), ReleaseApiModel("v1.1.3"))

        service.getVersions()
                .test()
                .assertValue{ it[0] == "1.1.2"}
                .assertValue{ it[1] == "1.1.3"}
    }

    private fun stubApiToReturn(vararg response: ReleaseApiModel) =
            whenever(api.listReleases()).doReturn(Observable.just(response.toList()))
}
