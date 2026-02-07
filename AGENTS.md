# AGENTS.md - Development Guidelines for Expenses Android App

## Project Overview
This is an Android expense tracking application built with Java using Android SDK 27, Gradle build system, and SQLite for data persistence. The app follows Android MVP-like patterns with DAO classes for database operations.

## Build/Test Commands

### Gradle Commands
```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests "luankevinferreira.expenses.dao.ExpenseDAOTest"

# Run specific test method
./gradlew test --tests "luankevinferreira.expenses.dao.ExpenseDAOTest.testShouldAddExpenseType"

# Generate code coverage
./gradlew jacocoTestReport

# Clean build
./gradlew clean

# Lint the code
./gradlew lint
```

### Testing
- **Unit Tests**: Located in `app/src/test/java/`
- **Instrumentation Tests**: Located in `app/src/androidTest/java/`
- **Test Framework**: JUnit 4 with Mockito for mocking
- **Android Test**: Espresso for UI tests

## Code Style Guidelines

### Package Structure
- Follow Android package conventions: `luankevinferreira.expenses.*`
- Organize by feature: `dao`, `domain`, `components`, `enumeration`, `util`

### Import Organization
1. Android framework imports
2. Java standard library imports  
3. Third-party library imports
4. Project-specific imports

Use static imports for constants and utility methods:
```java
import static android.view.View.OnClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static luankevinferreira.expenses.enumeration.CodeIntentType.STATUS_OK;
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `ExpenseActivity`, `ExpenseDAO`)
- **Methods**: camelCase (e.g., `saveExpense()`, `getSqLiteDatabase()`)
- **Variables**: camelCase (e.g., `expenseValue`, `expenseType`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `NO_FILTER_EN`, `QUERY_ERROR`)
- **Database columns**: UPPER_SNAKE_CASE (e.g., `TABLE_EXPENSE`, `EXPENSE_DATE`)

### Code Formatting
- 4-space indentation (no tabs)
- Braces on same line for class/method declarations
- Maximum line length: ~100 characters
- Use meaningful variable names, avoid abbreviations

### Error Handling
- Always close database connections in `finally` blocks
- Use try-catch blocks for database operations
- Return boolean for success/failure of database operations
- Show user-friendly Toast messages for errors
- Use proper exception handling patterns:

```java
try {
    dao.delete(expense);
    makeText(getApplicationContext(), getString(R.string.success_delete), LENGTH_LONG).show();
    setResult(CodeIntentType.STATUS_OK.getCode());
} catch (Exception exception) {
    setResult(CodeIntentType.STATUS_ERROR.getCode());
    makeText(getApplicationContext(), getString(R.string.error_delete), LENGTH_LONG).show();
} finally {
    dao.close();
}
```

### Database Best Practices
- Always close database connections
- Use ContentValues for insert/update operations
- Use parameterized queries to prevent SQL injection
- Implement proper cursor management (close in finally blocks)
- Use static constants for column names

### Android UI Patterns
- Implement OnClickListener interfaces where appropriate
- Use findViewById with proper null checks
- Follow Activity lifecycle patterns
- Use proper resource string references via `getString()`
- Support Up navigation with `setDisplayHomeAsUpEnabled(true)`

### Testing Patterns
- Use `@Before` and `@After` annotations for setup/teardown
- Use descriptive test method names following `testShould...` pattern
- Arrange-Act-Assert pattern in tests
- Clean up test data in tearDown
- Use appropriate test annotations (`@LargeTest`, `@RunWith`)

### Dependencies
- Android Support Library v27
- GraphView for charts
- JUnit 4 for testing
- Mockito for mocking
- PowerMock for static method testing

### Static Analysis
- Lint checks enabled with `abortOnError false`
- JaCoCo for code coverage
- Codacy integration for code quality
- Travis CI for continuous integration

### Localization
- Support multiple languages (EN/PT)
- Use string resources for all UI text
- Handle locale-specific formatting for dates and numbers