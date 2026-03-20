# AGENTS.md -- AI Agent Instructions

> Single source of truth for AI agents working on this repository.

## Project Overview

**attest-backend-interview-scenario** is a Java 17 console application used as a backend engineering interview take-home task at Attest. It provides a survey domain model with routed questions, static JSON data, and repository classes. Candidates are expected to implement query logic on top of the provided scaffolding.

## Tech Stack

| Component        | Detail                          |
|------------------|---------------------------------|
| Language         | Java 17                         |
| Build (Gradle)   | Gradle 8.5 (wrapper included)   |
| Build (Maven)    | Maven (candidate-installed)     |
| JSON library     | Jackson Databind 2.17.1         |
| Test framework   | JUnit 5 (Jupiter) 5.10.0        |
| Packaging        | Shadow plugin 8.3.3 (fat JAR)   |

## Commands

```bash
# Build, test, and run (Gradle -- preferred, no install needed)
./gradlew clean build run

# Build, test, and run (Maven)
mvn clean package exec:java

# Run tests only
./gradlew test        # Gradle
mvn test              # Maven
```

## Project Layout

```
.
├── AGENTS.md                          # This file
├── CLAUDE.md                          # Claude agent pointer file
├── docs/
│   └── ARCHITECTURE.md                # Architecture with Mermaid diagrams
├── .agents/rules/
│   ├── code-style.md                  # Code style conventions
│   ├── testing.md                     # Testing conventions
│   └── security.md                    # Security guidelines
├── build.gradle                       # Gradle build config
├── pom.xml                            # Maven build config
├── settings.gradle                    # Gradle settings
├── gradlew / gradlew.bat             # Gradle wrapper scripts
├── Readme.md                          # Interview instructions
├── Routes.md                          # Survey routing explanation
└── src/
    └── main/
        ├── java/com/askattest/interview/
        │   ├── Main.java              # Entry point
        │   ├── models/
        │   │   ├── Choice.java        # Answer choice POJO
        │   │   ├── Question.java      # Question POJO
        │   │   ├── Response.java      # Respondent answer POJO
        │   │   └── Survey.java        # Survey POJO
        │   └── repository/
        │       ├── ResponseRepo.java  # Response data loader & queries
        │       └── SurveyRepo.java    # Survey data loader & queries
        └── resources/survey-data/
            ├── responses.json         # Sample respondent answers
            ├── survey.json            # Survey definition with routing
            └── survey.mmd            # Mermaid routing diagram source
```

## Conventions

### Code Style
- See `.agents/rules/code-style.md` for full details.
- Java 17, 4-space indentation, public fields on POJOs, Jackson for JSON, Streams API for filtering.

### Testing
- See `.agents/rules/testing.md` for full details.
- JUnit 5, tests go in `src/test/java/` mirroring main source structure.
- No tests exist yet (by design -- this is an interview scaffold).

### Security
- See `.agents/rules/security.md` for full details.
- No network exposure, no secrets, all data is static JSON on classpath.

## Critical Rules

1. **Do not modify `Readme.md` or `Routes.md`** -- these are interview instructions given to candidates.
2. **Keep it framework-free** -- no Spring, Micronaut, or similar. The interview tests core Java skills.
3. **Maintain dual build system compatibility** -- changes must work with both Gradle and Maven.
4. **Models are plain POJOs** -- no Lombok, no getters/setters, public fields for Jackson field-level access.
5. **Data is loaded from classpath resources** (`/survey-data/`) -- do not introduce a database or external data source.
6. When updating documentation, keep `AGENTS.md`, `docs/ARCHITECTURE.md`, and `Readme.md` consistent with each other.
