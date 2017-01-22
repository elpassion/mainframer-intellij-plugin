import com.elpassion.intelijidea.MFConfigurationFactory
import com.elpassion.intelijidea.MFRunConfigurationType
import com.intellij.icons.AllIcons
import org.junit.Assert.assertEquals
import org.junit.Test

class MFConfigurationTypeTest {

    val configurationType = MFRunConfigurationType()

    @Test
    fun shouldHaveProperName() {
        assertEquals("Mainframer", configurationType.displayName)
    }

    @Test
    fun shouldHaveProperConfigurationTypeDescription() {
        assertEquals("Run project using mainframer", configurationType.configurationTypeDescription)
    }

    @Test
    fun shouldHaveProperConfigurationFactory() {
        assertEquals(MFConfigurationFactory::class.java, configurationType.configurationFactories.first().javaClass)
    }

    @Test
    fun shouldHaveProperIcon() {
        assertEquals(AllIcons.General.Information, configurationType.icon)
    }
}