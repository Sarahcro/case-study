package productservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

@Configuration
@ConfigurationProperties(value = "spring.data.cassandra")
public class CassandraConfig extends AbstractCassandraConfiguration {

  private String keySpaceName;
  private String contactPoints;
  private int port;
  private String username;
  private String password;

  @Override
  protected String getKeyspaceName() {
    return keySpaceName;
  }

  @Bean
  @Override
  public CqlSessionFactoryBean cassandraSession() {
    CqlSessionFactoryBean session = super.cassandraSession();
    session.setUsername(username);
    session.setPassword(password);
    return session;
  }
}
