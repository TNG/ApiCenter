[![Build Status](https://travis-ci.org/TNG/ApiCenter.svg?branch=master)](https://travis-ci.org/TNG/ApiCenter) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# ApiCenter

ApiCenter is a repository for OpenAPI and GraphQL specifications. Through the web interface you can upload and download specifications, interact with them through the [SwaggerUI](https://swagger.io/tools/swagger-ui/) / [GraphiQL](https://github.com/graphql/graphiql), and search their contents.
#### Specification overview
![Specification overview screenshot](https://user-images.githubusercontent.com/6897716/58478901-651c4480-8157-11e9-90d1-b4c3957c64d3.PNG)

#### SwaggerUI view 

![SwaggerUI view](https://user-images.githubusercontent.com/6897716/58482301-6866fe80-815e-11e9-9c76-3e748bb610fe.PNG)

More information and troubleshooting is on the [wiki](https://github.com/TNG/ApiCenter/wiki).

## How to run

### From source
* Clone the repository
* In a terminal, run `./gradlew :monoBuild`
* In a browser, go to `localhost:8080`

### With Docker
* Install Docker
* In a terminal, get ApiCenter's image from Docker Hub with `docker pull catenoid/apicenter:extdb`
* Run the image with `docker-compose up`
* In a browser, go to `localhost:8080`
* Stop the container with `docker-compose down` (add -v to clear the application data)

By default, user authentication is disabled. Login succeeds regardless of what password is entered.

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
