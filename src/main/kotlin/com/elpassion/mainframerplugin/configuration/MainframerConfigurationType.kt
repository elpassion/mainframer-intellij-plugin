package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.util.MainframerIcons
import com.intellij.execution.configurations.ConfigurationType
import javax.swing.Icon

class MainframerConfigurationType : ConfigurationType {

    override fun getDisplayName() = StringsBundle.getMessage("configuration.name")

    override fun getConfigurationTypeDescription() = StringsBundle.getMessage("configuration.description")

    override fun getIcon(): Icon = MainframerIcons.mainIcon

    override fun getId() = "MAINFRAMER_RUN_CONFIGURATION"

    override fun getConfigurationFactories() = arrayOf(MainframerConfigurationFactory(this))
}
