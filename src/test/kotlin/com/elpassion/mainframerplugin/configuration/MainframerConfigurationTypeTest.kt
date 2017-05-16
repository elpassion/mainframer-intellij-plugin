package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.util.MainframerIcons
import org.junit.Assert.assertEquals
import org.junit.Test

class MainframerConfigurationTypeTest {

    val configurationType = MainframerConfigurationType()

    @Test
    fun shouldHaveProperName() {
        assertEquals("Mainframer", configurationType.displayName)
    }

    @Test
    fun shouldHaveProperConfigurationTypeDescription() {
        assertEquals("Run project using Mainframer", configurationType.configurationTypeDescription)
    }

    @Test
    fun shouldHaveProperConfigurationFactory() {
        assertEquals(MainframerConfigurationFactory::class.java, configurationType.configurationFactories.first().javaClass)
    }

    @Test
    fun shouldHaveProperIcon() {
        assertEquals(MainframerIcons.mainIcon, configurationType.icon)
    }
}
