import com.elpassion.intelijidea.configuration.RemoteToLocalFileTranslator
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.junit.Assert

class RemoteToLocalFileTranslatorTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldTranslateRemoteFileNameToLocal() {
        val remotePathName = "/home/kasper/mainframer/light_temp/src/test/java/BB.java"
        val result = RemoteToLocalFileTranslator.translate(project, remotePathName)
        Assert.assertEquals("${project.basePath}/src/test/java/BB.java", result)
    }

    fun testReallyShouldTranslateRemoteFileNameToLocal() {
        val remotePathName = "/home/kasper/mainframer/light_temp/src/test/java/CC.java"
        val result = RemoteToLocalFileTranslator.translate(project, remotePathName)
        Assert.assertEquals("${project.basePath}/src/test/java/CC.java", result)
    }
}