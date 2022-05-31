# spring-restful-mongodb
Customer & order RESTful API. <br>
This repo is focusing mainly on different REST test api frameworks and Spring test annotations.

* @WebMVCTest
* @DataMongoTest
* @SpringBootTest
* REST-assured
* Testcontainers

# OpenAPI docs

* http://localhost:8080/swagger-ui.html

# Environment stuffs

## Testcontainers

* Docker installation is required.
* The testcontainers.properties file must be modified with the flag ``testcontainers.reuse.enable=true`` on your local
  machine.

## Run the "dev" environment
* Start the mongo container and redis ``docker-compose -f docker-compose.dev.yml up -d``
* Start the application with 'dev' spring profile active.

## Run the "prod" environment

* Build the .jar file ``mvn clean package``
* Build the docker image. ``docker build -t mangilaspringbootrestfulservice .``
* Start docker compose ``docker-compose up -d``
