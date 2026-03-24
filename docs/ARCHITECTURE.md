# Architecture

## Overview

This is a Java 17 console application used as a backend engineering interview take-home scenario at Attest. It models a **survey system** where respondents answer routed questions and earn payouts. The application loads survey definitions and response data from static JSON files and provides repository classes for querying them.

## High-Level Component Diagram

```mermaid
graph TD
    A[Main.java<br/>Entry Point] --> B[SurveyRepo]
    A --> C[ResponseRepo]
    B --> D[survey.json<br/>Classpath Resource]
    C --> E[responses.json<br/>Classpath Resource]
    B --> F[Survey Model]
    F --> G[Question Model]
    G --> H[Choice Model]
    C --> I[Response Model]
```

## Package Structure

```
com.askattest.interview
├── Main.java                  # Application entry point
├── models/
│   ├── Survey.java            # Survey entity (id, name, questions)
│   ├── Question.java          # Question entity (id, text, options, payout)
│   ├── Choice.java            # Answer choice (text, route, id)
│   └── Response.java          # Respondent answer (respondent, question, choice)
└── repository/
    ├── SurveyRepo.java        # Loads & queries survey data
    └── ResponseRepo.java      # Loads & queries response data
```

## Data Flow

```mermaid
sequenceDiagram
    participant Main
    participant SurveyRepo
    participant ResponseRepo
    participant Jackson as Jackson ObjectMapper
    participant FS as Classpath Resources

    Main->>SurveyRepo: new SurveyRepo()
    SurveyRepo->>Jackson: readValue(survey.json)
    Jackson->>FS: Load /survey-data/survey.json
    FS-->>Jackson: JSON content
    Jackson-->>SurveyRepo: Survey object

    Main->>ResponseRepo: new ResponseRepo()
    ResponseRepo->>Jackson: readValue(responses.json)
    Jackson->>FS: Load /survey-data/responses.json
    FS-->>Jackson: JSON content
    Jackson-->>ResponseRepo: List<Response>

    Main->>SurveyRepo: surveyById(200)
    SurveyRepo-->>Main: Survey

    Main->>ResponseRepo: responsesByRespondent(300)
    ResponseRepo-->>Main: List<Response>
```

## Survey Routing Model

The survey uses a **routed/branching** question flow. Each answer choice has an optional `route` field pointing to the next question ID. When `route` is absent (or `-1` in the Java model), the survey ends.

```mermaid
graph LR
    Q100[Q100: Fuel prices] -->|"answer 0"| Q101[Q101: Toothpaste brand]
    Q100 -->|"answers 1-4"| Q103[Q103: Flight priorities]
    Q101 -->|"all answers"| Q102[Q102: Bank switching]
    Q102 -->|"all answers"| Q105[Q105: Fuel 12mo]
    Q103 -->|"all answers"| Q104[Q104: England match]
    Q104 -->|"answers 0-2"| Q106[Q106: Green/LibDems]
    Q104 -->|"answers 3-4"| Q107[Q107: Shoe brands]
    Q105 -->|"all answers"| Q109[Q109: Cereal]
    Q106 -->|"all answers"| Q109
    Q107 -->|"all answers"| Q108[Q108: Phone type]
    Q108 -->|"all answers"| Q109
    Q109 -->|"all answers"| END((End))
```

## Domain Model

```mermaid
classDiagram
    class Survey {
        +int id
        +String name
        +List~Question~ questions
    }
    class Question {
        +int id
        +String text
        +List~Choice~ options
        +int payout
    }
    class Choice {
        +String text
        +int route
        +int id
    }
    class Response {
        +int respondent
        +int question
        +int choice
    }
    Survey "1" --> "*" Question
    Question "1" --> "*" Choice
```

## Build Systems

The project supports **two** build systems (candidate may choose either):

| Tool   | Config File     | Run Command                      |
|--------|-----------------|----------------------------------|
| Gradle | `build.gradle`  | `./gradlew clean build run`      |
| Maven  | `pom.xml`       | `mvn clean package exec:java`    |

## Key Dependencies

| Dependency         | Version  | Purpose                  |
|--------------------|----------|--------------------------|
| Jackson Databind   | 2.17.1   | JSON deserialisation     |
| JUnit 5 (Jupiter)  | 5.10.0   | Testing framework        |
| Shadow Plugin      | 8.3.3    | Fat JAR packaging (Gradle) |

## Design Decisions & Notes

- **No framework**: Deliberately kept framework-free (no Spring, Micronaut, etc.) to keep the interview focused on core Java skills.
- **Public fields on models**: POJOs use public fields rather than getters/setters for brevity. Jackson handles this via field-level access.
- **In-memory data**: All data is loaded into memory at startup from classpath JSON files. There is no database.
- **`Choice.route = -1`**: Sentinel value indicating the survey terminates after that choice.
