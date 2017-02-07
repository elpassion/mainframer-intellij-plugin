import com.elpassion.intelijidea.common.MFCommandLine
import com.elpassion.intelijidea.util.mfFilename
import org.junit.Assert
import org.junit.Test

class MFCommandLineTest {

    @Test
    fun shouldGenerateCommandLineToExecute() {
        val commandLine = MFCommandLine("./gradlew", "build")
        Assert.assertEquals("bash \"$mfFilename ./gradlew build\"", commandLine.commandLineString)
    }
}