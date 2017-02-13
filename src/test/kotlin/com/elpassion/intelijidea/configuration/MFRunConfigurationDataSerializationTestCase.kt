package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.util.fromJson
import com.elpassion.intelijidea.util.toJson
import junit.framework.TestCase

class MFRunConfigurationDataSerializationTestCase : TestCase() {

    fun testShouldDeserializeSerializedObject() {
        val data = MFRunConfigurationData(
                buildCommand = "buildCommand",
                taskName = "taskName",
                mainframerPath = "path")

        val json = data.toJson()
        val deserializedData = json.fromJson<MFRunConfigurationData>()
        assertEquals(data, deserializedData)
    }
}