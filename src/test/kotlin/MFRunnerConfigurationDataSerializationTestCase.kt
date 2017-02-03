import com.elpassion.intelijidea.configuration.MFRunnerConfigurationData
import com.elpassion.intelijidea.util.fromJson
import com.elpassion.intelijidea.util.toJson
import junit.framework.TestCase

class MFRunnerConfigurationDataSerializationTestCase : TestCase() {

    fun testShouldDeserializeSerializedObject() {
        val data = MFRunnerConfigurationData(
                buildCommand = "buildCommand",
                taskName = "taskName",
                mainframerPath = "path")

        val json = data.toJson()
        val deserializedData = json.fromJson<MFRunnerConfigurationData>()
        assertEquals(data, deserializedData)
    }
}