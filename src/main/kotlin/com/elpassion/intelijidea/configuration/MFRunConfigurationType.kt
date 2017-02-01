package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.util.MFIcons
import com.intellij.execution.configurations.ConfigurationType
import javax.swing.Icon

class MFRunConfigurationType : ConfigurationType {

    override fun getDisplayName() = "Mainframer"

    override fun getConfigurationTypeDescription() = "Run project using mainframer"

    override fun getIcon(): Icon = MFIcons.configurationIcon

    override fun getId() = "MAINFRAMER_RUN_CONFIGURATION"

    override fun getConfigurationFactories() = arrayOf(MFConfigurationFactory(this))
}
