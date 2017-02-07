import com.elpassion.intelijidea.common.MFCommandLine
import com.elpassion.intelijidea.util.mfFilename
import org.junit.Assert.assertEquals
import org.junit.Test

class MFCommandLineTest {

    @Test
    fun shouldGenerateCommandLineToExecute() {
        val commandLine = MFCommandLine("./gradlew", "build")
        commandLine.verify()
    }

    @Test
    fun shouldGenerateProperCommandLineToExecute() {
        val commandLine = MFCommandLine("gradle", "assembleDebug")
        commandLine.verify()
    }

    private fun MFCommandLine.verify() {
        assertEquals("bash \"$mfFilename $buildCommand $taskName\"", commandLineString)
    }
}