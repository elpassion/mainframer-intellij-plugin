package com.elpassion.intelijidea.action.configure

import com.elpassion.android.commons.rxjavatest.thenError
import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.android.commons.rxjavatest.thenNever
import com.elpassion.intelijidea.action.configure.configurator.MFToolInfo
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.io.File

class MFConfigureProjectActionControllerTest {

    private val mainframerReleasesFetcher = mock<() -> Single<List<String>>>()
    private val mainframerVersionChooser = mock<(List<String>) -> Maybe<MFToolInfo>>()
    private val mainframerFileDownloader = mock<(MFToolInfo) -> Maybe<Unit>>()
    private val showMessage = mock<(String) -> Unit>()
    private val uiScheduler = Schedulers.trampoline()
    private val progressScheduler = Schedulers.trampoline()
    private val controller = MFConfigureProjectActionController(mainframerReleasesFetcher, mainframerVersionChooser, mainframerFileDownloader, showMessage, uiScheduler, progressScheduler)

    @Test
    fun shouldConfigureMainframerInProject() {
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust(MFToolInfo("2.0.0", File("")))
        whenever(mainframerFileDownloader.invoke(any())).thenJust(Unit)

        controller.configureMainframer()

        verify(showMessage).invoke("Mainframer configured in your project!")
    }

    @Test
    fun shouldConfigureChosenVersionOfMainframer() {
        val chosenVersion = "2.0.0"
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust(MFToolInfo(chosenVersion, File("")))
        whenever(mainframerFileDownloader.invoke(any())).thenNever()

        controller.configureMainframer()

        verify(mainframerFileDownloader).invoke(argThat { version == chosenVersion })
    }

    @Test
    fun shouldPassDefaultMainframerPathToDownloader() {
        val defaultPath = File("defaultPath")
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust(MFToolInfo("2.0.0", defaultPath))
        whenever(mainframerFileDownloader.invoke(any())).thenNever()

        controller.configureMainframer()

        verify(mainframerFileDownloader).invoke(argThat { file.path == defaultPath.path })
    }

    @Test
    fun shouldShowFetchedVersionsOfMainframer() {
        val fetchedVersions = listOf("2.0.0", "1.0.0")
        whenever(mainframerReleasesFetcher.invoke()).thenJust(fetchedVersions)
        whenever(mainframerVersionChooser.invoke(any())).thenNever()

        controller.configureMainframer()

        verify(mainframerVersionChooser).invoke(fetchedVersions)
    }

    @Test
    fun shouldShowErrorWhenDownloadFails() {
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust(MFToolInfo("2.0.0", File("")))
        whenever(mainframerFileDownloader.invoke(any())).thenError()

        controller.configureMainframer()

        verify(showMessage).invoke("Error during mainframer configuration")
    }

    @Test
    fun shouldShowErrorWhenFetchFails() {
        whenever(mainframerReleasesFetcher.invoke()).thenError()

        controller.configureMainframer()

        verify(showMessage).invoke("Error during mainframer configuration")
    }
}
