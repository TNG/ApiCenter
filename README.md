# ApiCenter

ApiCenter is an API specification repository. It is the sole resting place of all your [OpenAPI](https://www.openapis.org/) (formerly known as [Swagger](https://swagger.io/)) and [GraphQL](https://graphql.org/) specifications, so you never have to go look for them anymore.
You can upload your specs, search through them, test them with [SwaggerUI](https://swagger.io/tools/swagger-ui/) or [GraphiQL](https://github.com/graphql/graphiql) and download them.

Gone are the days where specs might hide in some obscure folder in your source code, or somewhere in some wiki. All specs in one place, with all the comfort you want.

## How to run

### From source

- Clone the repository
- In a terminal, run `./gradlew :runAll`
- In a browser, go to `localhost:8080`

By default, user authentication is disabled. Login succeeds regardless of what password is entered.

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
