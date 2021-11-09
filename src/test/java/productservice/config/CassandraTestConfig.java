package productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

@Configuration
public class CassandraTestConfig extends CassandraConfig {

    @Bean
    @Override
    @DependsOn("container")
    public CqlSessionFactoryBean cassandraSession() {
        return super.cassandraSession();
    }

    @Bean
    public FixedPortCassandraContainer container() {
        var cassandraContainer = new FixedPortCassandraContainer();
        cassandraContainer.start();
        return cassandraContainer;
    }
}
