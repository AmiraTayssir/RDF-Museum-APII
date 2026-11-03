# RDF Reader

## Prerequisites

- Java 21

## Run

From the project directory, start the application with the Maven wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

The API is available at `http://localhost:8080/professors`. It reads professor
data from `src/main/resources/professeur.rdf`.

## Test

```powershell
.\mvnw.cmd test
```
