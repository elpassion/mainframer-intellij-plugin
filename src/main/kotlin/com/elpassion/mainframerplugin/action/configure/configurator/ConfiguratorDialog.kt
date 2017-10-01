package com.elpassion.mainframerplugin.action.configure.configurator

import com.elpassion.mainframerplugin.action.configure.configurator.ui.ConfiguratorForm
import com.elpassion.mainframerplugin.common.*
import com.elpassion.mainframerplugin.common.ui.InsertMacroActionListener
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import io.reactivex.Maybe
import javax.swing.JComponent

class ConfiguratorDialog(project: Project,
                         val defaultValues: ConfiguratorIn,
                         doOnOk: (ConfiguratorOut) -> Unit,
                         doOnCancel: () -> Unit) : DialogWrapperAdapter<ConfiguratorOut>(project, doOnOk, doOnCancel) {

    private val form = ConfiguratorForm()

    init {
        title = StringsBundle.getMessage("configure.configurator.dialog.title")
        init()
        form.insertMacroButton.addActionListener(InsertMacroActionListener(form.buildCommandField, project))
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

    override fun getSuccessResult() = ConfiguratorOut(
            version = form.versionComboBox.selectedItem.toString(),
            buildCommand = form.buildCommandField.text,
            remoteName = form.remoteMachineField.text)
}

fun configurationDialog(project: Project): (ConfiguratorIn) -> Maybe<ConfiguratorOut> {
    return { defaultValues ->
        showConfigurationDialog(project, defaultValues)
    }
}

private fun showConfigurationDialog(project: Project, defaultValues: ConfiguratorIn): Maybe<ConfiguratorOut> =
        Maybe.create<ConfiguratorOut> { emitter ->
            ConfiguratorDialog(project, defaultValues, {
                emitter.onSuccess(it)
            }, {
                emitter.onComplete()
            }).show()
        }