package productservice.integration


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.GenericContainer
import productservice.config.CassandraTestConfig
import productservice.config.FixedPortCassandraContainer
import spock.lang.Specification

@ActiveProfiles('itspec')
@SpringBootTest(webEnvironment  = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseITSpec extends Specification {

    MockMvc mockMvc

    @Autowired
    FixedPortCassandraContainer container

    @Autowired
    WebApplicationContext context

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()

    }
    def cleanup(){
        container.stop()
    }

    def 'application loads'(){
        expect: 'loads'
        context != null
    }
}
