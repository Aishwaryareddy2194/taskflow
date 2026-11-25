# TaskFlow – Multi-Module Java To-Do Application

TaskFlow is a small, three-tier task management application prepared as the application component of a DevOps portfolio project. The repository intentionally contains the developer-side application only; add your own Jenkinsfile and CI/CD infrastructure as a separate exercise.

## Architecture

- **Presentation tier:** Spring MVC + Thymeleaf + HTML/CSS.
- **Business tier:** `TaskService` in `taskflow-core`.
- **Data tier:** Spring Data JPA repositories with H2 locally and optional MySQL.
- **Build:** Maven multi-module project.

## Modules

| Module | Responsibility |
|---|---|
| `taskflow-core` | Task entity, status enum, repository and business service |
| `taskflow-web` | Spring Boot entry point, MVC controller, Thymeleaf UI and runtime configuration |
| Parent POM | Shared Java version, Spring Boot dependency versions and module aggregation |

The web module depends on core. Build from the root directory so Maven reactor builds modules in the correct order.

## Requirements

- JDK 17+
- Maven 3.8+
- Internet access for the first dependency download

## Build and test

From the repository root:

```bash
mvn clean verify
```

The executable Spring Boot JAR is produced at:

```text
taskflow-web/target/taskflow-web-1.0.0.jar
```

Run it:

```bash
java -jar taskflow-web/target/taskflow-web-1.0.0.jar
```

Open `http://localhost:8080`.

## Local database

The default profile uses a file-backed H2 database at `./data/taskflow`. Tasks remain available after an application restart. The H2 console is available at `/h2-console` during local development.

## Optional MySQL profile

Create a MySQL database/user, then run:

```bash
SPRING_PROFILES_ACTIVE=mysql DB_URL='jdbc:mysql://localhost:3306/taskflow' DB_USERNAME='taskflow' DB_PASSWORD='your-secret' java -jar taskflow-web/target/taskflow-web-1.0.0.jar
```

Do not commit real credentials. Configure them as Jenkins credentials or environment variables in a deployment environment.

## Useful endpoints

- `/` – TaskFlow dashboard and task management UI
- `/actuator/health` – Health check for deployment verification
- `/h2-console` – Local H2 console (development only)

## Features

- Create, edit, delete and complete tasks
- Filter all, pending and completed tasks
- Dashboard counts for total, pending and completed tasks
- Input constraints and basic error handling
- Sample tasks on first startup
- Unit tests for core task service and a Spring application context test

## DevOps exercise boundary

This repository does **not** include a Jenkinsfile, webhook configuration, SonarQube configuration, dependency vulnerability scanner configuration, Dockerfile, or AWS deployment scripts. Those are intentionally left for the DevOps pipeline exercise.

Suggested pipeline stages to implement separately:

1. Checkout source from GitHub
2. Compile and test with Maven
3. Run SonarQube analysis and quality gate
4. Run dependency vulnerability scanning (for example, OWASP Dependency-Check)
5. Package the executable JAR
6. Deploy the JAR to an EC2 instance
7. Verify `http://<EC2-host>:8080/actuator/health`

## Important security note

This is a portfolio/training application, not a production-ready multi-user service. It does not implement user authentication or authorization. The H2 console should be disabled in production, secrets must be externalized, and access to the application should be restricted appropriately. Add Spring Security before exposing it to untrusted users.
