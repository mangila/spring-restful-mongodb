package com.github.mangila.springbootrestfulservice.web.resource.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@Testcontainers
@Slf4j
public abstract class SeededMongoDBTestContainer {

    private static final MongoDBContainer container =
            new MongoDBContainer(DockerImageName.parse("mongo:5.0.2"))
                    .withClasspathResourceMapping(
                            "customer-test-mongoimport-schema.json",
                            "/tmp/customer-test-mongoimport-schema.json",
                            BindMode.READ_ONLY)
                    .withClasspathResourceMapping(
                            "order-test-mongoimport-schema.json",
                            "/tmp/order-test-mongoimport-schema.json",
                            BindMode.READ_ONLY
                    )
                    .withLogConsumer(new Slf4jLogConsumer(log))
                    // 'testcontainers.reuse.enable=true' flag must be set in testcontainers.properties
                    .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        container.start();
        var execResult = container.execInContainer("mongoimport", "--db", "test",
                "--drop", "--collection", "customer", "--file", "/tmp/customer-test-mongoimport-schema.json", "--jsonArray");
        Assertions.assertEquals(0, execResult.getExitCode());
        execResult = container.execInContainer("mongoimport", "--db", "test",
                "--drop", "--collection", "order", "--file", "/tmp/order-test-mongoimport-schema.json", "--jsonArray");
        Assertions.assertEquals(0, execResult.getExitCode());
    }
}
