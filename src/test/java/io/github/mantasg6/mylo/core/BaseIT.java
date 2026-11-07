package io.github.mantasg6.mylo.core;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class BaseIT {

    @SuppressWarnings("resource")
	@ServiceConnection
    protected static final PostgreSQLContainer<?> container = 
            new PostgreSQLContainer<>("postgres:15").withDatabaseName("mylo");

    static {
        container.start();
    }
}
