package jackson

import com.elpassion.intelijidea.util.listFromJson
import org.junit.Assert
import org.junit.Test
import java.io.Serializable

class JacksonExtensionsTests {

    @Test
    fun shouldParseMainframerVersionsList() {
        val listFromJson = "[{\"tag_name\": \"v1.1.2\" }]".listFromJson<TagNameWrapper>()
        Assert.assertEquals("v1.1.2", listFromJson[0].tagName)
    }

    @Test
    fun shouldNotFailWhenJsonHasMoreFieldThanModel() {
        val listFromJson = "[{\"tag_name\": \"v1.1.2\" ,\"ignore_field\": \"ignore_value\" }]".listFromJson<TagNameWrapper>()
        Assert.assertEquals("v1.1.2", listFromJson[0].tagName)
    }

    @Test
    fun shouldUseParseCamelCase() {
        val listFromJson = "[{\"description_info\": \"description\"}]".listFromJson<TagNameWrapper>()
        Assert.assertEquals("description", listFromJson[0].descriptionInfo)
    }

}

class TagNameWrapper : Serializable {
    var tagName: String = ""
    var descriptionInfo: String = ""

}