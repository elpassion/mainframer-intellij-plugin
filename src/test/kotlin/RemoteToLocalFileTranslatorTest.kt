import com.elpassion.intelijidea.configuration.RemoteToLocalFileTranslator
import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteToLocalFileTranslatorTest {

    private val project = mock<Project> {
        on { basePath } doReturn "/local/path/to/$PROJECT_NAME"
        on { name } doReturn PROJECT_NAME
    }

    @Test
    fun testShouldTranslateRemoteFileNameToLocal() {
        val remotePathName = "/home/kasper/mainframer/$PROJECT_NAME/src/test/java/BB.java"
        val result = RemoteToLocalFileTranslator.translate(project, remotePathName)
        assertEquals("${project.basePath}/src/test/java/BB.java", result)
    }

    @Test
    fun testReallyShouldTranslateRemoteFileNameToLocal() {
        val remotePathName = "/home/kasper/mainframer/$PROJECT_NAME/src/test/java/CC.java"
        val result = RemoteToLocalFileTranslator.translate(project, remotePathName)
        assertEquals("${project.basePath}/src/test/java/CC.java", result)
    }

    companion object {
        private val PROJECT_NAME = "testProject"
    }
}