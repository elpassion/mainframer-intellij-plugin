package com.elpassion.intelijidea

import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import javax.swing.Icon


class MFRunConfigurationType : ConfigurationType {

    override fun getDisplayName() = "Mainframer"

    override fun getConfigurationTypeDescription() = "Run project using mainframer"

    override fun getIcon(): Icon = AllIcons.General.Information

    override fun getId() = "MAINFRAMER_RUN_CONFIGURATION"

    override fun getConfigurationFactories() = arrayOf(MFConfigurationFactory(this))
}
