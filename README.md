# ApiCenter

ApiCenter keeps track of all the services that run in your organization.

It is a API specification repository. You can upload all your [OpenAPI](https://www.openapis.org/) (formerly known as [Swagger](https://swagger.io/)) specifications, so you never have to go look for them anymore. Search through them, test them with [SwaggerUI](https://swagger.io/tools/swagger-ui/) and download them. All in one central place.

It is an interface repository. Some services have more than one interface, or they maintain two versions of the same interface, or something else. ApiCenter can manage them all.

It is, finally, a service repository. If you ever wanted to know which services depend on yours, ApiCenter can help you with the answer. It can also tell you which version they use, to make your changes easier. And, if you want to consume a new service, ApiCenter is also there for you, showing you their interfaces and specs and maybe letting you know about a new version (in the future).

## How to run

### From source

- Clone the repository
- In a terminal, run `./gradlew :runAll`
- In a browser, go to `localhost:8080`

### With Docker

We do not have an official Docker image yet. Until then, you can build your own by cloning the repository and executing the following steps:

- `./gradlew bootBuildImage`
- `docker run -p 8080:8080 tngtech/apicenter`

This will start ApiCenter in a Docker container with an in-memory DB.

## Built with

- [Gradle](https://gradle.org/)
- [Kotlin](https://kotlinlang.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [TypeScript](https://www.typescriptlang.org/)
- [Angular](https://angular.io/)

## Contributing

Please see [CONTRIBUTING.md](CONTRIBUTING.md)

## Versioning

ApiCenter uses [semantic versioning](https://semver.org/). You should expect breakage if the major version changes.

## License

ApiCenter is licensed under the [Apache 2.0 license](https://github.com/tngtech/apicenter/LICENSE.md).
