# ApiCenter

ApiCenter is a repository for all your API specifications. You can upload new ones and search them so that finding the one you need is easy.

## Getting started

ApiCenter consists of a RESTful backend service written in Kotlin and a SPA frontend in Angular. Both are contained in this git repo.

### Backend
In order to start the backend, clone the repo, navigate to the `backend` folder and start the service with the familiar Spring Boot command:
```
cd /backend
./gradlew bootRun
```

### Frontend
To start the frontend, clone the repo, navigate to the `frontend` folder and start it with angular CLI:
```
cd /frontend
ng serve
```

## Prerequisites

To run ApiCenter, you need `gradle` and angular CLI. The backend itself needs a relational database, for example PostgreSQL.

To install angular CLI, enter `npm install -g @angular/cli`

## Running the tests

Running the included tests is simply `./gradlew test` for the backend and `` for the frontend

## Code Style & Linting

## Deployment


## Built with
- [Gradle](https://gradle.org/)
- [Kotlin](https://kotlinlang.org/)
- [Spring Boot 2](https://spring.io/projects/spring-boot)
- [Angular](https://angular.io/)

## Contributing
See CONTRIBUTING.md

## Versioning
ApiCenter uses [semantic versioning](https://semver.org/). You can expect breakage if the major version changes.

## License
ApiCenter is licensed unter the [Apache 2.0 license](https://github.com/tngtech/apicenter/LICENSE.md).
