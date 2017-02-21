package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.action.configure.downloader.downloadFileToProject
import com.elpassion.intelijidea.util.getLatestMfToolDownloadUrl
import com.elpassion.intelijidea.util.mfFilename
import com.elpassion.intelijidea.util.showError
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.project.Project
import java.io.File
import javax.swing.event.HyperlinkEvent

class MFConfigurationFactory(configurationType: ConfigurationType) : ConfigurationFactory(configurationType) {

    override fun createTemplateConfiguration(project: Project) = MFRunConfiguration(
            project = project,
            configurationFactory = this,
            name = "Mainframer",
            showToolNotFoundError = { mainframerPath ->
                showError(project, fileNotFoundErrorMessage(mainframerPath)) {
                    if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        downloadFileToProject(project, it.url.toString(), File(project.basePath, mfFilename))
                    }
                }
            })

    override fun getName() = FACTORY_NAME

    companion object {
        private val FACTORY_NAME = "Factory for mainframer configuration"

        private fun fileNotFoundErrorMessage(mainframerPath: String?) =
                "Cannot find <b>$mfFilename</b> in the following path:\n\"$mainframerPath\"\n\n" +
                        "<a href=\"${getLatestMfToolDownloadUrl()}\">Download latest mainframer tool</a>"
    }
}
