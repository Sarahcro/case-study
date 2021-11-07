package productservice.config;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class FixedPortCassandraContainer extends GenericContainer {

    public FixedPortCassandraContainer(){
        super("cassandra:3");
        this.addFixedExposedPort(9042, 9042);
        this.addFixedExposedPort(7000, 7000);
    }
}
