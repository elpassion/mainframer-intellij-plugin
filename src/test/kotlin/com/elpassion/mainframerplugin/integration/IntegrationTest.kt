package com.elpassion.mainframerplugin.integration

import com.elpassion.mainframerplugin.task.MainframerTaskProvider
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.testFramework.LightIdeaTestCase
import org.junit.Assert

class IntegrationTest : LightIdeaTestCase() {

    fun testShouldHaveMainframerOnExtensionList() {
        Assert.assertEquals(
                MainframerTaskProvider.TASK_NAME,
                getJavaFacade().project.getExtensions(BeforeRunTaskProvider.EXTENSION_POINT_NAME).last().name
        )
    }
}