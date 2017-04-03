package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.util.toJson
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.*
import org.jdom.Element
import org.junit.Assert

class MFBeforeRunTaskTest : LightPlatformCodeInsightFixtureTestCase() {
    val element = mock<Element>()

    override fun setUp() {
        super.setUp()
        MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = MFTaskData()
    }

    fun testShouldReadDefaultValuesFromProviderWhenWasntSaveBefore() {
        val task = MFBeforeRunTask(data = MFTaskData())
        task.readExternal(element)

        Assert.assertEquals(MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData, task.data)
    }

    fun testShouldReadMFTaskDataFromJson() {
        val task = MFBeforeRunTask(data = MFTaskData())
        whenever(element.getAttributeValue(any())).thenReturn(MFTaskData(buildCommand = "b", mainframerPath = "p").toJson())
        task.readExternal(element)

        Assert.assertEquals(MFTaskData(buildCommand = "b", mainframerPath = "p"), task.data)
    }

    fun testShouldWriteMFTaskDataToJson() {
        val task = MFBeforeRunTask(MFTaskData(buildCommand = "b", mainframerPath = "p"))
        task.writeExternal(element)
        verify(element).setAttribute(any(), eq(task.data.toJson()))
    }
}