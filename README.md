[![Build Status](https://travis-ci.org/TNG/ApiCenter.svg?branch=master)](https://travis-ci.org/TNG/ApiCenter) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# ApiCenter

ApiCenter is a repository for OpenAPI and GraphQL specifications. Through the web interface you can upload and download specifications, interact with them through the [SwaggerUI](https://swagger.io/tools/swagger-ui/) / [GraphiQL](https://github.com/graphql/graphiql), and search their contents.
#### Specification overview
![Specification overview screenshot](https://user-images.githubusercontent.com/6897716/59667744-7f29cf80-91b7-11e9-8223-ba0398944589.PNG)

#### SwaggerUI view 

![SwaggerUI view](https://user-images.githubusercontent.com/6897716/59667749-805afc80-91b7-11e9-96d9-089bc1906993.PNG)

More information and troubleshooting is on the [wiki](https://github.com/TNG/ApiCenter/wiki).

#### Demo file upload
![Demo file upload](https://user-images.githubusercontent.com/6897716/59666747-bdbe8a80-91b5-11e9-8676-eaccb12e24d3.gif)

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
