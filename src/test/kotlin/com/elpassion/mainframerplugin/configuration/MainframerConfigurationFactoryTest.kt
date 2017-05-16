package com.elpassion.mainframerplugin.configuration

import com.intellij.openapi.command.impl.DummyProject
import org.junit.Assert.assertEquals
import org.junit.Test

class MainframerConfigurationFactoryTest {

    val confFactory = MainframerConfigurationFactory(MainframerConfigurationType())

    @Test
    fun shouldHaveProperName() {
        assertEquals("Factory for Mainframer configuration", confFactory.name)
    }

    @Test
    fun shouldCreateProperRunConfiguration() {
        assertEquals(MainframerRunConfiguration::class.java, confFactory.createTemplateConfiguration(DummyProject.getInstance()).javaClass)
    }
}
