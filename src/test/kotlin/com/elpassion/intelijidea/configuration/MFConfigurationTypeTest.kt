package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.util.MFIcons
import org.junit.Assert.assertEquals
import org.junit.Test

class MFConfigurationTypeTest {

    val configurationType = MFRunConfigurationType()

    @Test
    fun shouldHaveProperName() {
        assertEquals("Mainframer", configurationType.displayName)
    }

    @Test
    fun shouldHaveProperConfigurationTypeDescription() {
        assertEquals("Run project using mainframer", configurationType.configurationTypeDescription)
    }

    @Test
    fun shouldHaveProperConfigurationFactory() {
        assertEquals(MFConfigurationFactory::class.java, configurationType.configurationFactories.first().javaClass)
    }

    @Test
    fun shouldHaveProperIcon() {
        assertEquals(MFIcons.mainframerIcon, configurationType.icon)
    }
}