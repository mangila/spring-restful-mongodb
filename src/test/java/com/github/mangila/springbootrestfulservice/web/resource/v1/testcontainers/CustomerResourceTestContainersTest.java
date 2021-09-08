package com.github.mangila.springbootrestfulservice.web.resource.v1.testcontainers;

import org.junit.jupiter.api.Tag;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Tag("testcontainers")
@Testcontainers
public class CustomerResourceTestContainersTest {

    @Container
    final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
}
