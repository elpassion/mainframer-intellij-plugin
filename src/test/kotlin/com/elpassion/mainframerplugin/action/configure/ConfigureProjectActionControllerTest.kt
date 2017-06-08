package com.elpassion.mainframerplugin.action.configure

import com.elpassion.android.commons.rxjavatest.thenError
import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.android.commons.rxjavatest.thenNever
import com.elpassion.mainframerplugin.action.configure.configurator.ToolInfo
import com.intellij.openapi.util.io.FileUtil
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ConfigureProjectActionControllerTest {

    private val releasesFetcher = mock<() -> Single<List<String>>>()
    private val versionChooser = mock<(List<String>) -> Maybe<ToolInfo>>()
    private val fileDownloader = mock<(ToolInfo) -> Maybe<File>>()
    private val showMessage = mock<(String) -> Unit>()
    private val showError = mock<(String) -> Unit>()
    private val uiScheduler = Schedulers.trampoline()
    private val progressScheduler = Schedulers.trampoline()
    private val controller = ConfigureProjectActionController(releasesFetcher, versionChooser, fileDownloader, showMessage, showError, uiScheduler, progressScheduler, mock())

    @Test
    fun shouldConfigureMainframerInProject() {
        whenever(releasesFetcher.invoke()).thenJust("2.0.0")
        whenever(versionChooser.invoke(any())).thenJust(ToolInfo("2.0.0", File("")))
        whenever(fileDownloader.invoke(any())).thenJust(File(""))

        controller.configureMainframer()

        verify(showMessage).invoke("Mainframer configured in your project!")
    }

    @Test
    fun shouldConfigureChosenVersionOfMainframer() {
        val chosenVersion = "2.0.0"
        whenever(releasesFetcher.invoke()).thenJust("2.0.0")
        whenever(versionChooser.invoke(any())).thenJust(ToolInfo(chosenVersion, File("")))
        whenever(fileDownloader.invoke(any())).thenNever()

        controller.configureMainframer()

        verify(fileDownloader).invoke(argThat { version == chosenVersion })
    }

    @Test
    fun shouldPassDefaultMainframerPathToDownloader() {
        val defaultPath = File("defaultPath")
        whenever(releasesFetcher.invoke()).thenJust("2.0.0")
        whenever(versionChooser.invoke(any())).thenJust(ToolInfo("2.0.0", defaultPath))
        whenever(fileDownloader.invoke(any())).thenNever()

        controller.configureMainframer()

        verify(fileDownloader).invoke(argThat { file.path == defaultPath.path })
    }

    @Test
    fun shouldShowFetchedVersionsOfMainframer() {
        val fetchedVersions = listOf("2.0.0", "1.0.0")
        whenever(releasesFetcher.invoke()).thenJust(fetchedVersions)
        whenever(versionChooser.invoke(any())).thenNever()

        controller.configureMainframer()

        verify(versionChooser).invoke(fetchedVersions)
    }

    @Test
    fun shouldShowErrorWhenDownloadFails() {
        whenever(releasesFetcher.invoke()).thenJust("2.0.0")
        whenever(versionChooser.invoke(any())).thenJust(ToolInfo("2.0.0", File("")))
        whenever(fileDownloader.invoke(any())).thenError()

        controller.configureMainframer()

        verify(showError).invoke("Error during Mainframer configuration")
    }

    @Test
    fun shouldShowErrorWhenFetchFails() {
        whenever(releasesFetcher.invoke()).thenError()

        controller.configureMainframer()

        verify(showError).invoke("Error during Mainframer configuration")
    }

    @Test
    fun shouldGrandExecutePermissionToFileAfterConfigureMainframer() {
        whenever(releasesFetcher.invoke()).thenJust("2.0.0")
        val outputFile = FileUtil.createTempFile(File("tempFile"), "mainframer.sh", null)
        whenever(versionChooser.invoke(any())).thenJust(ToolInfo("2.0.0", outputFile))
        whenever(fileDownloader.invoke(any())).thenJust(outputFile)

        controller.configureMainframer()

        assertTrue(outputFile.canExecute())
    }
}
