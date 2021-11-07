package productservice.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * Note
 */
@EqualsAndHashCode(callSuper = true)
@Configuration
@Data
@EnableCassandraRepositories(basePackages = "productservice.repository")
@ConfigurationProperties(value = "spring.data.cassandra")
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
  @DependsOn("container")
  public CqlSessionFactoryBean cassandraSession() {
    CqlSessionFactoryBean session = super.cassandraSession();
    session.setUsername(username);
    session.setPassword(password);
    return session;
  }

  @Override
  public SchemaAction getSchemaAction(){
    return schemaAction;
  }
  
  @Override
  public String[] getEntityBasePackages(){
    return new String[] {"productservice.entity"};
  }

 //@Override
 //protected List<CreateKeyspaceSpecification> getKeyspaceCreations(){
   //var specification = CreateKeyspaceSpecification.createKeyspace(keySpaceName);
    //return List.of(specification);
//  }
}
