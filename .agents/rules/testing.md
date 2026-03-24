# Testing Rules

## Framework
- **JUnit 5** (Jupiter) is declared as a test dependency in both `build.gradle` and `pom.xml`.
- JUnit BOM version: `5.10.0`
- Gradle is configured with `useJUnitPlatform()`.

## Current State
- **No tests currently exist.** The `src/test/` directory is absent from the repository.
- This is an interview take-home scenario, so the lack of tests is by design for the candidate to address.

## Conventions for Writing Tests
- Place tests under `src/test/java/com/askattest/interview/` mirroring the main source structure.
- Use JUnit 5 annotations (`@Test`, `@BeforeEach`, etc.).
- Test class names should end with `Test` (e.g., `SurveyRepoTest`, `ResponseRepoTest`).

## Running Tests
```bash
# Gradle
./gradlew test

# Maven
mvn test
```
