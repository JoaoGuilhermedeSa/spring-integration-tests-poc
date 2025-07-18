# Spring Boot Kotlin POC: Game Items API

This is a Proof of Concept (POC) for a simple REST API using Spring Boot and Kotlin. The API manages items in a game, ensuring no duplicate items by name. It uses a database-backed repository, with PostgreSQL for runtime and H2 in-memory for tests.

---

## Technologies Used

- **Spring Boot**: Version 3.3.1 – Framework for building the web application and REST API.
- **Kotlin**: Version 2.0.0 – Primary programming language for concise and safe code.
- **Spring Data JPA**: For ORM and repository abstraction.
- **PostgreSQL**: Database for production/runtime (driver version 42.7.7).
- **H2 Database**: In-memory database for integration tests (test scope).
- **Maven**: Build tool for dependency management and project lifecycle.
- **Jackson Kotlin Module**: For JSON serialization/deserialization with Kotlin data classes.
- **Docker Compose**: To spin up a PostgreSQL instance easily.
- **TestRestTemplate**: For integration testing the REST endpoints in Spring Boot tests.

---

## Code Structure

The project follows a standard Maven structure with Kotlin sources:

```
src/main/kotlin/com/example/
    ├── Application.kt              # Main entry point for the Spring Boot application
    ├── Item.kt                    # JPA entity representing a game item (with unique name constraint)
    ├── ItemRepository.kt          # Spring Data JPA repository interface for CRUD operations
    └── ItemController.kt          # REST controller with endpoints for creating and listing items

src/main/resources/
    └── application.properties     # Runtime config (PostgreSQL datasource, JPA settings)

src/test/kotlin/com/example/
    └── ItemControllerIntegrationTest.kt  # Integration tests with @SpringBootTest and TestRestTemplate

src/test/resources/
    └── application.properties     # Test config (H2 datasource)

pom.xml                            # Maven config with Kotlin and Spring Boot dependencies
docker-compose.yml                 # (Optional) For PostgreSQL container setup
```

---

## How to Run the Application

### Prerequisites

- Java 21 (JDK)
- Maven 3.x
- Docker (for PostgreSQL, optional but recommended)

### Steps

#### 1. Start PostgreSQL

Use the provided `docker-compose.yml` to start a PostgreSQL container:

```bash
docker-compose up -d
```

This creates a database `gamedb` with user `user` and password `password` on `localhost:5432`.

Verify:

```bash
docker ps  # Look for container `game-postgres`
```

#### 2. Build and Run

```bash
mvn clean install          # Compile the project
mvn spring-boot:run        # Start the application
```

The app runs at: [http://localhost:8080](http://localhost:8080)

#### 3. Stop the Application

- Stop the app: `Ctrl+C`
- Stop PostgreSQL: `docker-compose down`

---

## Testing the API with `curl`

The API has two endpoints under `/items`:

- `POST /items`: Create a new item  
  Returns `201 Created` on success, `409 Conflict` if the name already exists.
  
- `GET /items`: List all items  
  Returns `200 OK` with a JSON array.

### Examples

#### Create a New Item

```bash
curl -X POST -H "Content-Type: application/json"   -d '{"name": "Espada Mágica", "description": "Uma espada poderosa"}'   http://localhost:8080/items
```

**Expected Response**:
```json
{
  "id": 1,
  "name": "Espada Mágica",
  "description": "Uma espada poderosa"
}
```

---

#### Attempt Duplicate Creation

- Run the same POST command again.

**Expected Response**:  
`409 Conflict` (no body)

---

#### List All Items

```bash
curl http://localhost:8080/items
```

**Expected Response**:
```json
[
  {
    "id": 1,
    "name": "Espada Mágica",
    "description": "Uma espada poderosa"
  }
]
```

---

## Running the Unit Tests

The project includes integration tests using an in-memory H2 database (no PostgreSQL required).

- Run all tests:

```bash
mvn test
```

- Or build with tests:

```bash
mvn clean install
```

### Tests include:

- ✅ Successful item creation
- ❌ Conflict on duplicate name
- 📋 Listing items after creation

All tests should pass if the setup is correct.

For verbose output:

```bash
-Dspring-boot.run.arguments="--logging.level.org.springframework=DEBUG"
```