package productservice.config;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@EqualsAndHashCode(callSuper = true)
@Configuration
@Data
@EnableCassandraRepositories(basePackages = "productservice.repository")
@ConfigurationProperties(value = "spring.data.cassandra")
@Profile("!itspec")
public class CassandraConfig extends AbstractCassandraConfiguration {

  private String keySpaceName;
  private String contactPoints;
  private int port;
  private String username;
  private String password;
  private SchemaAction schemaAction;

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
    session.setKeyspaceStartupScripts(List.of("CREATE KEYSPACE IF NOT EXISTS " + keySpaceName +
        " WITH replication = \n" +
        "{'class':'SimpleStrategy','replication_factor':'1'};"));
    return session;
  }

  @Override
  public SchemaAction getSchemaAction() {
    return schemaAction;
  }

  @Override
  public String[] getEntityBasePackages() {
    return new String[]{"productservice.entity"};
  }

}
