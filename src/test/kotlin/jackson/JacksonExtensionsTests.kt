package jackson

import com.elpassion.intelijidea.util.listFromJson
import org.junit.Assert
import org.junit.Test
import java.io.Serializable

class JacksonExtensionsTests {

    @Test
    fun shouldParseMainframerVersionsList() {
        val listFromJson = "[{\"tag_name\": \"v1.1.2\" }]".listFromJson<TagNameWrapper>()
        Assert.assertEquals("v1.1.2", listFromJson[0].tag_name)
    }
}

class TagNameWrapper : Serializable {
    var tag_name: String = ""
}