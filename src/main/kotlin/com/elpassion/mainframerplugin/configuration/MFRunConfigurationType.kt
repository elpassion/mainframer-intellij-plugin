package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.util.MFIcons
import com.intellij.execution.configurations.ConfigurationType
import javax.swing.Icon

class MFRunConfigurationType : ConfigurationType {

    override fun getDisplayName() = StringsBundle.getMessage("configuration.name")

    override fun getConfigurationTypeDescription() = StringsBundle.getMessage("configuration.description")

    override fun getIcon(): Icon = MFIcons.mainframerIcon

    override fun getId() = "MAINFRAMER_RUN_CONFIGURATION"

    override fun getConfigurationFactories() = arrayOf(MFConfigurationFactory(this))
}
