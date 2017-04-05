package com.elpassion.mainframerplugin.action.configure.configurator

import com.elpassion.mainframerplugin.action.configure.configurator.ui.MConfiguratorForm
import com.elpassion.mainframerplugin.common.*
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import io.reactivex.Maybe
import javax.swing.JComponent

class MFConfiguratorDialog(project: Project,
                           val defaultValues: MFConfiguratorIn,
                           doOnOk: (MFConfiguratorOut) -> Unit,
                           doOnCancel: () -> Unit) : DialogWrapperAdapter<MFConfiguratorOut>(project, doOnOk, doOnCancel) {

    private val form = MConfiguratorForm()

    init {
        title = "Configure for Project"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.versionComboBox.model = CollectionComboBoxModel(defaultValues.versionList)
        form.buildCommandField.text = defaultValues.buildCommand
        form.remoteMachineField.text = defaultValues.remoteName
        return form.panel
    }

    override fun doValidate() = with(form) {
        validateForm(RemoteMachineFieldValidator(remoteMachineField), BuildCommandValidator(buildCommandField))
    }

    override fun getSuccessResult() = MFConfiguratorOut(
            version = form.versionComboBox.selectedItem.toString(),
            buildCommand = form.buildCommandField.text,
            remoteName = form.remoteMachineField.text)
}

fun mfConfigurationDialog(project: Project): (MFConfiguratorIn) -> Maybe<MFConfiguratorOut> {
    return { defaultValues ->
        showConfigurationDialog(project, defaultValues)
    }
}

private fun showConfigurationDialog(project: Project, defaultValues: MFConfiguratorIn): Maybe<MFConfiguratorOut> =
        Maybe.create<MFConfiguratorOut> { emitter ->
            MFConfiguratorDialog(project, defaultValues, {
                emitter.onSuccess(it)
            }, {
                emitter.onComplete()
            }).show()
        }
