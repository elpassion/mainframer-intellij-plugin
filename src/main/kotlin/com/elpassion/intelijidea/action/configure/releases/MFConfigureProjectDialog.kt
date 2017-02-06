package com.elpassion.intelijidea.action.configure.releases

import com.elpassion.intelijidea.action.configure.releases.api.provideGithubApi
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubRetrofit
import com.elpassion.intelijidea.action.configure.releases.service.MFVersionsReleaseService
import com.elpassion.intelijidea.action.configure.releases.ui.MFConfigureProjectForm
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent


class MFConfigureProjectDialog(private val project: Project, private val onVersion: (String) -> Unit) : DialogWrapper(project, false) {

    private val form = MFConfigureProjectForm()
    private val service = MFVersionsReleaseService(provideGithubApi(provideGithubRetrofit()))

    init {
        title = "Configure Mainframer in Project"
        init()
    }

    override fun createCenterPanel(): JComponent {
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Downloading mainframer version") {
            override fun run(indicator: ProgressIndicator) {
                val releaseVersionsList = service.getVersions()
                form.versionComboBox.model = MFComboBoxViewModel(releaseVersionsList)
            }
        })
        return form.panel
    }

    override fun doOKAction() {
        super.doOKAction()
        val selectedVersion = form.versionComboBox.selectedItem.toString()
        onVersion(selectedVersion)
    }
}
