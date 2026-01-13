# Survey Earnings Calculator

A Java application that calculates respondent earnings based on survey responses using dependency injection and caching for optimal performance.

## Project Overview

This application processes survey data to calculate how much each respondent has earned by answering questions. It demonstrates:

- **Dependency Injection** using PicoContainer
- **Caching Strategy** for efficient question lookups
- **Clean Architecture** with separation of concerns (repositories, services, caching)
- **Interface-based Design** for testability and flexibility
- **Comprehensive Unit Tests** using JUnit 5 and Mockito

## Architecture

### Design Patterns

- **Repository Pattern**: Abstracts data access logic
- **Dependency Injection**: Manages object dependencies using PicoContainer
- **Caching**: Question cache to avoid repeated survey traversals
- **Interface Segregation**: All components have interfaces for loose coupling

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher

## Building and Running

### Build the project

```bash
./gradlew build
```

### Run tests

```bash
./gradlew test
```

### Run the application

```bash
./gradlew run
```

## How It Works

### 1. Data Loading
- `SurveyRepo` loads survey data from `survey.json`
- `ResponseRepo` loads response data from `responses.json`

### 2. Question Caching
- `QuestionCacheRepo` creates a Map of question IDs to Question objects
- Cache is lazy-loaded on first use
- Eliminates need to traverse survey structure repeatedly

### 3. Earnings Calculation
- `EarningService` processes all responses
- For each response, looks up the question payout from cache
- Aggregates earnings per respondent using `RespondentEarning` model

## Testing Strategy

### Unit Tests
- **QuestionCacheRepoTest**: Tests caching behavior, lazy loading, and cache invalidation
- **EarningServiceTest**: Tests earnings calculation logic with mocked dependencies
- **RespondentEarningTest**: Tests the model's increment and formatting logic

### Test Coverage
- Repository caching logic
- Service business logic
- Edge cases (missing questions, empty responses, zero payouts)
- Mock-based isolation for true unit testing

## Key Features

### 1. Efficient Caching
The `QuestionCacheRepo` ensures questions are only loaded once:
- First call: Loads all questions from surveys into a HashMap
- Subsequent calls: O(1) lookup from cache
- Prevents repeated nested loop traversals

### 2. Dependency Injection
Using PicoContainer for automatic dependency resolution:
- No manual object creation
- Easy to swap implementations
- Simplified testing with mocks

### 3. Clean Separation of Concerns
- **Models**: Plain data objects
- **Repositories**: Data access and caching
- **Services**: Business logic
- Each layer depends only on interfaces

## Performance Considerations

- **Question Lookup**: O(1) with caching vs O(n×m) without (n=surveys, m=questions per survey)
- **Memory**: Questions cached in memory for duration of application
- **Lazy Loading**: Cache only populated when first needed

## Future Enhancements

- Add logging framework (SLF4J + Logback)
- Implement cache eviction strategies (LRU, TTL)
- Add configuration management (different data sources)
- REST API layer for web access
- Database persistence instead of JSON files