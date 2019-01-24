[![Build Status](https://travis-ci.org/TNG/ApiCenter.svg?branch=master)](https://travis-ci.org/TNG/ApiCenter) [![Waffle.io - Columns and their card count](https://badge.waffle.io/TNG/ApiCenter.svg?columns=In%20Progress)](https://waffle.io/TNG/ApiCenter) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# ApiCenter

ApiCenter is a repository for all your OpenAPI specifications. You can upload new ones and search them so that finding the one you need is easy.

## Prerequisites

### General

To run ApiCenter, you need `gradle` and angular CLI. The backend itself needs a relational database, for example PostgreSQL. It provides an in-memory database if not configured otherwise.

To install angular CLI, enter `npm install -g @angular/cli`

### User Authentication

Currently ApiCenter can use Atlassian Crowd as an authentication provider, or disregard user authentication entirely for those who want to try the service without setting up Crowd (in this case, any user can log in with any username, the password field is simply ignored).

In `backend/src/main/resources/application.properties`, this is configured by the line:
```
auth.service=crowd
```
Writing `auth.service=none`, or deleting the line (not specifying an authentication service), will remove user authentication.

#### Atlassian Crowd
To use Atlassian Crowd as an authentication provider, [add ApiCenter
as an external application in Atlassian Crowd](https://confluence.atlassian.com/crowd/adding-an-application-18579591.html#AddinganApplication-add) and configure
the following files within ApiCenter:

- `backend/src/main/resources/crowd.properties`
```
application.name=### Set the name of the application as specified in the Atlassian Crowd Console ###
application.password=### Set the password of the application as specified in the Atlassian Crowd Console ###
crowd.server.url=### Set the URL to the Atlassian Crowd Server service-endpoint, e.g. http://localhost:8095/crowd/services/ ###
```

- `backend/src/main/resources/application.properties`
```
jwt.secret=### Define a secret that is used to sign JWTs ###
```

We plan to add OAuth 2 support shortly.

## Getting started

ApiCenter consists of a RESTful backend service written in Kotlin and a SPA frontend in Angular. Both are contained in this git repo. There are two ways to run ApiCenter: one combines the tasks of the frontend and backend together, for simplicity; the other requires two terminal sessions, however has advantages for developers.

For both, ensure that the authentication service is running.
### Combined
From the project root directory, run:
```
./gradlew :monoBuild
```
The `:monoBuild` task is composed of three subtasks, which can be independently invoked if needed:
- `:frontend:buildClientDev` runs Angular's build command, producing the output files in the default location `frontend/dist`.
- `:frontend:copyFrontendFiles` copies these files into a directory in `backend` from which Spring will serve static resources.
- `:backend:bootRun` will compile and run the backend.

The service is available from `localhost:8080` (Spring boot's default port).

### Separate

The combined command is simpler to run, however has disadvantages to developers, who are likely to make modifications to the source files. Any change to frontend or backend requires re-running `bootRun`, which recompiles both projects. The in-memory database used by the backend will be reset.

For the frontend, this is unnecessary. When developing in Angular, `ng serve` will handily watch for changes to the source files, and the live development site will auto-update to reflect them.

Also, since the frontend build task depends on `npm install`, every run of `:frontend:buildClientDev` will do `npm`'s somewhat time consuming and unskippable package audit. Without this task, `npm i` can run independently of `ng serve` (or `ng build`).

#### Backend
To start the backend, from the project root directory, run:
```
./gradlew :backend:bootRun
```

#### Frontend
To start the frontend, from the project root directory, run:
```
./gradlew :frontend:ngServe
```
The service is available from `localhost:4200` (Angular's default port).
## Running the tests

### Backend
Running the backend tests is `./gradlew test` for all Unit-Tests and `./gradlew integrationTest` for all Integration-Tests.

### Frontend
Running all frontend tests is simply `ng test`, however ensure you are within the `frontend` directory.

## Mutation Testing
In order to keep the quality of our tests high, we regularly execute mutation testing tools on our code for quality assurance. 
Sadly, the automatic support of reporting tools is not sophisticated enough yet, so we rely on manual execution and monitoring. 
If you want to execute the tools yourself in order to determine the mutation score (maybe for pull requests), execute the following commands:

- Mutation testing in the backend with [pitest](http://pitest.org/): `./gradlew pitest`
- Mutation testing in the frontend with [Stryker](https://stryker-mutator.io/): `stryker run` (make sure that you have the Striker CLI installed, otherwise run: `npm install -g stryker-cli`)

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
ApiCenter is licensed unter the [Apache 2.0 license](https://github.com/tngtech/apicenter/LICENSE.md).
