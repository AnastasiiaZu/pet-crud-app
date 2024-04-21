# Welcome to PetApp

This app allows you to create, read, update, or delete a pet of your choice. The storage is in-memory.

## API

See [Swagger](Swagger.yaml) for detailed API documentation.

## Requirements

The project is written in Scala 2.13. I recommend using  [Java 17](https://adoptium.net/en-GB/) or higher.

The build system is SBT - I am using the latest version, currently 1.9.9.

I use Akka HTTP in this implementation.

## Test

```console
$ sbt test
```

## Run

```console
$ sbt run
```

The application starts on `localhost:8080` by default.

## Improvements for production

1) Deployment - can be deployed with [Heroku](https://devcenter.heroku.com/)
2) For storage, a simple SQL solution with `petId` as primary key is sufficient
3) Implement authorisation with JWT in auth headers
4) Store secrets in an external key-vault

