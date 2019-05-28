[![Build Status](https://travis-ci.org/TNG/ApiCenter.svg?branch=master)](https://travis-ci.org/TNG/ApiCenter) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# ApiCenter

ApiCenter is a repository for OpenAPI and GraphQL specifications. Through the web interface you can upload & download specifications, interact with them through the [SwaggerUI](https://swagger.io/tools/swagger-ui/) / [GraphiQL](https://github.com/graphql/graphiql), and search their contents.

More information and troubleshooting is on the [wiki](https://github.com/TNG/ApiCenter/wiki).

## How to run

### From source
* Clone the repository
* In a terminal, run `./gradlew :monoBuild`
* In a browser, go to `localhost:8080`

### With Docker
* Install Docker
* In a terminal, get ApiCenter's image from Docker Hub with `docker pull catenoid/apicenter`
* Run the image with `docker run catenoid/apicenter -P`
* Find out your docker container's ID with `docker ps` (which lists all running containers)
* Find out your docker container's IP address with: (it will probably be 172.17.0.2)

`docker inspect -f "{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}" <your-container-ID>`

* In a browser, go to `<that-IP-Address>:8080`

By default, user authentication is disabled. Login succeeds regardless of what password is entered.

## How to test

Run backend unit tests with `./gradlew test`
Run backend integration tests with `./gradlew integrationTest`

Run frontend tests with `ng test -c=localhost`, however ensure you are within the `frontend` directory.

## Built with
- [Gradle](https://gradle.org/)
- [Kotlin](https://kotlinlang.org/)
- [Spring Boot 2](https://spring.io/projects/spring-boot)
- [Angular](https://angular.io/)

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md)

## Versioning
ApiCenter uses [semantic versioning](https://semver.org/). You can expect breakage if the major version changes.

## License
ApiCenter is licensed under the [Apache 2.0 license](https://github.com/tngtech/apicenter/LICENSE.md).
