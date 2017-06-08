package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.util.toJson
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.*
import org.jdom.Element
import org.junit.Assert

class MainframerTaskTest : LightPlatformCodeInsightFixtureTestCase() {
    val element = mock<Element>()

    override fun setUp() {
        super.setUp()
        MainframerTaskDefaultSettingsProvider.getInstance(project).taskData = TaskData()
    }

    fun testShouldReadDefaultValuesFromProviderWhenWasntSaveBefore() {
        val task = MainframerTask(data = TaskData())
        task.readExternal(element)

        Assert.assertEquals(MainframerTaskDefaultSettingsProvider.getInstance(project).taskData, task.data)
    }

    fun testShouldReadTaskDataFromJson() {
        val task = MainframerTask(data = TaskData())
        whenever(element.getAttributeValue(any())).thenReturn(TaskData(buildCommand = "b", mainframerPath = "p").toJson())
        task.readExternal(element)

        Assert.assertEquals(TaskData(buildCommand = "b", mainframerPath = "p"), task.data)
    }

    fun testShouldWriteTaskDataToJson() {
        val task = MainframerTask(TaskData(buildCommand = "b", mainframerPath = "p"))
        task.writeExternal(element)
        verify(element).setAttribute(any(), eq(task.data.toJson()))
    }
}