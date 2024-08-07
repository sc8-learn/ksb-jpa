package schin.blog

import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.assertj.core.api.Assertions.assertThat
import mu.KotlinLogging
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(
    classes = arrayOf(BlogApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HtmlControllerTest {
    private val logger = KotlinLogging.logger {}

    @LocalServerPort
    private val serverPort = 0

    @BeforeAll
    fun setupFirst(){
        RestAssured.port=serverPort
    }

    @Test
    fun blogMethodOneTest() {
        val response =
        When {
            get("/")
        } Then {
            statusCode(200)
            body("html.head.title", equalTo("Blog"),
                "html.body",  contains("Blog")
            )
        }
    }

    @Test
    fun blogMethodTwoTest() {
        val response =
            When {
                get("/")
            } Then {
                statusCode(200)
            } Extract {
                response()
            }

        val html = response.htmlPath()
        assertThat(html.getString("html.head.title")).isEqualTo("Blog")
        assertThat(html.getString("html.body")).isEqualTo("Blog")
    }

    /**
     * https://stackoverflow.com/questions/33167541/checking-html-document-with-rest-assured
     * To get json out of the body
     *   String bodyTxt = response.htmlPath().getString("body");//Get the body element of the html response.
     *     JsonPath jsonObj = new JsonPath(bodyTxt);            //helps us to find things in a json string.
     *
     *     List<String> rootItems = jsonObj.getList("$");//get root element of the json part.
     *
     */
}