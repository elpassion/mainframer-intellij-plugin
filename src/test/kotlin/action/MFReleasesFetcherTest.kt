package action

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.ReleaseApiModel
import com.elpassion.intelijidea.action.configure.releases.service.MFReleasesFetcher
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

        val versions = service.getVersions().blockingFirst()
        assertEquals("1.1.2", versions.first())
    }

    @Test
    fun shouldProperMapReleaseApiModelToString() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"), ReleaseApiModel("v1.1.3"))

        val versions = service.getVersions().blockingFirst()

        assertEquals("1.1.2", versions[0])
        assertEquals("1.1.3", versions[1])
    }

    private fun stubApiToReturn(vararg response: ReleaseApiModel) = whenever(api.listReleases())
            .doReturn(Observable.just(response.toList()))
}
