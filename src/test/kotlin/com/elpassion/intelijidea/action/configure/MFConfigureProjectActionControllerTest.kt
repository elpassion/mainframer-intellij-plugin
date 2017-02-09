package com.elpassion.intelijidea.action.configure

import com.elpassion.android.commons.rxjavatest.thenJust
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
    private val mainframerVersionChooser = mock<(List<String>) -> Observable<Result<String>>>()
    private val mainframerFileDownloader = mock<(String) -> Observable<Result<Unit>>>()
    private val showMessage = mock<(String) -> Unit>()
    private val uiScheduler = Schedulers.trampoline()
    private val progressScheduler = Schedulers.trampoline()
    private val controller = MFConfigureProjectActionController(mainframerReleasesFetcher, mainframerVersionChooser, mainframerFileDownloader, showMessage, uiScheduler, progressScheduler)

    @Test
    fun shouldConfigureMainframerInProject() {
        whenever(mainframerReleasesFetcher.invoke()).thenJust("2.0.0")
        whenever(mainframerVersionChooser.invoke(any())).thenJust(Result.Success("2.0.0"))
        whenever(mainframerFileDownloader.invoke(any())).thenJust(Result.Success(Unit))

        controller.configureMainframer()

        verify(showMessage).invoke("Mainframer configured in your project!")
    }
}
