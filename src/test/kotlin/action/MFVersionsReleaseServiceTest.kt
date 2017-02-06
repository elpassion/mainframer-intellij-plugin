package action

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.ReleaseApiModel
import com.elpassion.intelijidea.action.configure.releases.service.MFVersionsReleaseService
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import tools.response

class MFVersionsReleaseServiceTest() {
    val api = mock<GitHubApi>()
    val service = MFVersionsReleaseService(api)

    @Test
    fun shouldDropFirstCharacter() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"))

        val versions = service.getVersions()
        assertEquals("1.1.2", versions.first())
    }

    @Test
    fun shouldProperMapReleaseApiModelToString() {
        stubApiToReturn(ReleaseApiModel("v1.1.2"), ReleaseApiModel("v1.1.3"))

        val versions = service.getVersions()

        assertEquals("1.1.2", versions[0])
        assertEquals("1.1.3", versions[1])
    }

    private fun stubApiToReturn(vararg response: ReleaseApiModel) = whenever(api.listReleases())
            .doReturn(response(response.toList()))
}
