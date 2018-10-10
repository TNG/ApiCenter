[![Build Status](https://travis-ci.org/TNG/ApiCenter.svg?branch=master)](https://travis-ci.org/TNG/ApiCenter) [![Waffle.io - Columns and their card count](https://badge.waffle.io/TNG/ApiCenter.svg?columns=In%20Progress)](https://waffle.io/TNG/ApiCenter) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# ApiCenter

ApiCenter is a repository for all your OpenAPI specifications. You can upload new ones and search them so that finding the one you need is easy.

## Prerequisites

To run ApiCenter, you need `gradle` and angular CLI. The backend itself needs a relational database, for example PostgreSQL.

To install angular CLI, enter `npm install -g @angular/cli`

## Getting started

ApiCenter consists of a RESTful backend service written in Kotlin and a SPA frontend in Angular. Both are contained in this git repo.

### Prerequisites
#### Atlassian Crowd
ApiCenter currently requires Atlassian Crowd as an authentication provider. In order to make this work, you need to [add ApiCenter
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

### Backend
In order to start the backend, clone the repo, navigate to the `backend` folder and start the service with the familiar Spring Boot command:
```
cd backend/
./gradlew bootRun
```

### Frontend
To start the frontend, clone the repo, navigate to the `frontend` folder and start it with angular CLI:
```
cd frontend/
npm install
ng serve
```

## Running the tests

Running the included tests is simply `./gradlew test` for the backend and `ng test` for the frontend

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
