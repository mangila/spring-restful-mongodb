package com.github.mangila.springbootrestfulservice.web.resource.v1.testcontainers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@Testcontainers
public abstract class PredefinedMongoDBTestContainer {

    @Container
    private static final MongoDBContainer container =
            new MongoDBContainer(DockerImageName.parse("mongo:5.0.2"))
                    .withClasspathResourceMapping(
                            "customer-test-schema.json",
                            "/tmp/customer-test-schema.json",
                            BindMode.READ_ONLY)
                    .withClasspathResourceMapping(
                            "order-test-schema.json",
                            "/tmp/order-test-schema.json",
                            BindMode.READ_ONLY
                    );

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        var execResult = container.execInContainer("mongoimport", "--db", "test",
                "--collection", "customer", "--file", "/tmp/customer-test-schema.json", "--jsonArray");
        Assertions.assertEquals(0, execResult.getExitCode());
        execResult = container.execInContainer("mongoimport", "--db", "test",
                "--collection", "order", "--file", "/tmp/order-test-schema.json", "--jsonArray");
        Assertions.assertEquals(0, execResult.getExitCode());
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }
}
