package productservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.testcontainers.containers.GenericContainer;

import java.time.Duration;

@Configuration
@Slf4j
public class CassandraTestConfig {

    @Bean
    public FixedPortCassandraContainer container(){
        var cassandraContainer = new FixedPortCassandraContainer();
        cassandraContainer.start();
        return cassandraContainer;
    }
}
