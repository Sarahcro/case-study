package productservice.config;

import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class FixedPortCassandraContainer extends CassandraContainer {

    public FixedPortCassandraContainer(){
        super("cassandra:3");
        this.addFixedExposedPort(9142, 9042);
    }
}
