package com.elpassion.intelijidea.action.configure

import com.elpassion.android.commons.rxjavatest.thenError
import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.android.commons.rxjavatest.thenNever
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.io.File

class MFConfigureProjectActionControllerTest {

    private val mainframerReleasesFetcher = mock<() -> Single<List<String>>>()
    private val mainframerVersionChooser = mock<(List<String>) -> Maybe<Pair<String, File>>>()
    private val mainframerFileDownloader = mock<(String, File) -> Maybe<Unit>>()
    private val showMessage = mock<(String) -> Unit>()
    private val uiScheduler = Schedulers.trampoline()
    private val progressScheduler = Schedulers.trampoline()
    private val controller = MFConfigureProjectActionController(mainframerReleasesFetcher, mainframerVersionChooser, mainframerFileDownloader, showMessage, uiScheduler, progressScheduler)

    @Test
    fun shouldConfigureMainframerInProject() {
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust("2.0.0" to File(""))
        whenever(mainframerFileDownloader.invoke(any(), any())).thenJust(Unit)

        controller.configureMainframer()

        verify(showMessage).invoke("Mainframer configured in your project!")
    }

    @Test
    fun shouldConfigureChosenVersionOfMainframer() {
        val chosenVersion = "2.0.0"
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust(chosenVersion to File(""))
        whenever(mainframerFileDownloader.invoke(any(), any())).thenNever()

        controller.configureMainframer()

        verify(mainframerFileDownloader).invoke(eq(chosenVersion), any())
    }

    @Test
    fun shouldPassDefaultMainframerPathToDownloader() {
        val defaultPath = File("defaultPath")
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust("2.0.0" to defaultPath)
        whenever(mainframerFileDownloader.invoke(any(), any())).thenNever()

        controller.configureMainframer()

        verify(mainframerFileDownloader).invoke(any(), eq(defaultPath))
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
        whenever(mainframerVersionChooser.invoke(any())).thenJust("2.0.0" to File(""))
        whenever(mainframerFileDownloader.invoke(any(), any())).thenError()

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
