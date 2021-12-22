# spring-restful-mongodb

Spring Boot Java 11 RESTful service with aggregation and date relationship in the domain model. Following DTO
architecture pattern and MVC with a service layer. A simple order/customer API with C.R.U.D operations. This repo is
focusing mainly on different REST test api frameworks and Spring test annotations. Spring Docs are being used for API
documentation.

* @WebMVCTest
* @DataMongoTest
* @SpringBootTest
* REST-assured
* Testcontainers

# OpenAPI docs

When running the application of course :P

* http://localhost:8080/api/swagger-ui.html

# Environment stuffs

## Testcontainers

* Docker installation is required.
* The testcontainers.properties file must be modified with the flag ``testcontainers.reuse.enable=true`` on your local
  machine.

## Run the "dev" environment

If you have a local installation of MongoDB and/or Redis just make the application-dev.properties file point at your
local installations or change the docker-compose.dev files port number. Else do this

* Start the mongo container and redis ``docker-compose -f docker-compose.dev.yml up -d``
* Start the application with 'dev' spring profile active.

## Run the "prod" environment

* Build the .jar file ``mvn clean package``
* Build the docker image. ``docker build -t mangilaspringbootrestfulservice .``
* Start docker compose ``docker-compose up -d``
* The poor man's production environment should be up and running :P 
