# Code Style Rules

## Language & Version
- Java 17 (source compatibility set in both `build.gradle` and `pom.xml`)

## Naming Conventions (verified from codebase)
- **Packages**: lowercase dot-separated (`com.askattest.interview`, `com.askattest.interview.models`, `com.askattest.interview.repository`)
- **Classes**: PascalCase (`SurveyRepo`, `ResponseRepo`, `Main`)
- **Fields**: camelCase, public visibility on model/POJO classes (`respondent`, `question`, `choice`)
- **Methods**: camelCase (`surveyById`, `responsesByRespondent`, `loadResponses`)

## Project Structure
```
src/main/java/com/askattest/interview/
  Main.java                      # Entry point
  models/                        # POJOs (Jackson-deserializable)
    Choice.java
    Question.java
    Response.java
    Survey.java
  repository/                    # Data-loading / query layer
    ResponseRepo.java
    SurveyRepo.java
src/main/resources/survey-data/  # Static JSON data files
```

## Patterns Observed
- **Models are plain POJOs** with public fields (no getters/setters, no Lombok). Jackson deserialises via field access.
- **Repositories** load JSON data from classpath resources in their constructors using Jackson `ObjectMapper`.
- **No dependency injection framework** is in use; classes are instantiated manually in `Main`.
- **`toString()`** overrides use `String.format()`.
- **Default field values** used where appropriate (e.g., `Choice.route = -1` to signal "no route").
- **Streams API** used for filtering collections.

## Formatting
- 4-space indentation (consistent across all Java files)
- Opening braces on same line as declaration
- Single blank line between methods
