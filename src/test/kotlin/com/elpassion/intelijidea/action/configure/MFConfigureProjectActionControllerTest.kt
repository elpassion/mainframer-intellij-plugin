package com.elpassion.intelijidea.action.configure

import com.elpassion.android.commons.rxjavatest.thenError
import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.android.commons.rxjavatest.thenNever
import com.elpassion.intelijidea.common.Result
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class MFConfigureProjectActionControllerTest {

    private val mainframerReleasesFetcher = mock<() -> Observable<List<String>>>()
    private val mainframerVersionChooser = mock<(List<String>) -> Observable<String>>()
    private val mainframerFileDownloader = mock<(String) -> Observable<Result<Unit>>>()
    private val showMessage = mock<(String) -> Unit>()
    private val uiScheduler = Schedulers.trampoline()
    private val progressScheduler = Schedulers.trampoline()
    private val controller = MFConfigureProjectActionController(mainframerReleasesFetcher, mainframerVersionChooser, mainframerFileDownloader, showMessage, uiScheduler, progressScheduler)

    @Test
    fun shouldConfigureMainframerInProject() {
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust("2.0.0")
        whenever(mainframerFileDownloader.invoke(any())).thenJust(Result.Success(Unit))

        controller.configureMainframer()

        verify(showMessage).invoke("Mainframer configured in your project!")
    }

    @Test
    fun shouldConfigureChosenVersionOfMainframer() {
        val chosenVersion = "2.0.0"
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust(chosenVersion)
        whenever(mainframerFileDownloader.invoke(any())).thenNever()

        controller.configureMainframer()

        verify(mainframerFileDownloader).invoke(chosenVersion)
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
        whenever(mainframerVersionChooser.invoke(any())).thenJust("2.0.0")
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
