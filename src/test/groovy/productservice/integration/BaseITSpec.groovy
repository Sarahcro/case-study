package productservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.spock.Testcontainers
import productservice.config.FixedPortCassandraContainer
import spock.lang.Specification

@Testcontainers
@ActiveProfiles('itspec')
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseITSpec extends Specification {

    MockMvc mockMvc

    ObjectMapper objectMapper = new ObjectMapper()

    @Autowired
    FixedPortCassandraContainer container

    @Autowired
    WebApplicationContext context

    WireMockServer wireMockServer = new WireMockServer(6767)

    def setup() {
        wireMockServer.start()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

    }

    def cleanup() {
        wireMockServer.stop()
        container.stop()
    }
}
