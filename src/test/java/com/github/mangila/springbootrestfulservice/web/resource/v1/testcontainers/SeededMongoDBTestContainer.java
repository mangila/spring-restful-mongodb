package com.github.mangila.springbootrestfulservice.web.resource.v1.testcontainers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@Testcontainers
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
                    .withReuse(true); // 'testcontainers.reuse.enable=true' needs to be set
                                      // - if you are running on your local machine

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
