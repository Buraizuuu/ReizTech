# Selenium BDD Framework

A Behavior-Driven Development (BDD) test automation framework built with **Selenium 4**, **Cucumber 7**, and **JUnit 4** in Java. Features a proper **Page Object Model**, configurable browser/headless execution via system properties, automatic failure screenshots, and a **GitHub Actions CI/CD pipeline**.

> **Application Under Test:** [OrangeHRM Live Demo](https://opensource-demo.orangehrmlive.com/)

---

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 21 | Core language |
| Maven | 3.x | Build & dependency management |
| Selenium | 4.33.0 | Browser automation |
| Cucumber | 7.20.1 | BDD framework (Gherkin scenarios) |
| JUnit | 4 | Test runner |
| WebDriverManager | 6.1.0 | Automatic browser driver management |
| DataFaker | 2.4.3 | Dynamic test data generation |
| Jackson Databind | 2.15.2 | JSON config parsing |

---

## What Was Improved

- **Page Object Model** — `pages/LoginPage.java` and `pages/DirectoryPage.java` replace the old static locator bags. Each page class extends `BasePage`, owns its locators as private fields, and exposes clean action methods (`login()`, `isDashboardVisible()`, etc.)
- **Hooks moved to `utility/`** — `utility/Hooks.java` is now a plain class (no `extends BrowserDriver`), using `BrowserDriver.setupDriver()` / `BrowserDriver.closeDriver()` via static calls for clean lifecycle management
- **Configurable browser & headless** — `BrowserDriver` reads `browser` and `headless` from system properties (`-Dbrowser`, `-Dheadless`) with environment variable fallbacks, so no source edits are needed to switch browsers or run headless
- **CI/CD pipeline** — `.github/workflows/maven.yml` runs the full suite headless on every push/PR, caches Maven dependencies, and uploads Cucumber reports and failure screenshots as artifacts
- **`locators/` package deprecated** — the old `locators/LoginPage.java` and `locators/DirectoryPage.java` (static `By` bags) are superseded by the `pages/` package and kept only for reference

---

## Project Structure

```
Selenium-BDD/
├── .github/
│   └── workflows/
│       └── maven.yml              # GitHub Actions CI pipeline
├── pom.xml
└── src/
    └── test/
        └── java/
            ├── features/
            │   └── Login.feature          # Gherkin test scenarios
            ├── locators/                  # Deprecated — superseded by pages/
            │   ├── LoginPage.java
            │   └── DirectoryPage.java
            ├── pages/                     # Page Object Model
            │   ├── LoginPage.java         # Login page actions + locators
            │   └── DirectoryPage.java     # Directory page actions + locators
            ├── resources/
            │   └── config/
            │       └── config.json        # Environment configuration
            ├── runner/
            │   └── TestRunner.java        # Cucumber JUnit runner
            ├── stepDefinition/
            │   ├── LoginPageSteps.java    # Login step implementations
            │   └── DirectoryPageSteps.java# Directory step implementations
            └── utility/
                ├── BasePage.java          # Reusable WebDriver action wrappers
                ├── BrowserDriver.java     # WebDriver factory (Chrome/Firefox/Edge)
                ├── ConfigReader.java      # Reads config.json at runtime
                ├── FakeDataUtil.java      # Random test data via DataFaker
                └── Hooks.java             # Before/After hooks + screenshots
```

---

## Prerequisites

- **Java 21** or higher — [Download](https://adoptium.net/)
- **Maven 3.6+** — [Download](https://maven.apache.org/download.cgi)
- **Google Chrome** (default browser) — or Firefox / Edge
- No manual ChromeDriver download needed — WebDriverManager handles it automatically

---

## Configuration

Edit `src/test/java/resources/config/config.json`:

```json
{
  "BASE_URL": "https://opensource-demo.orangehrmlive.com/",
  "USERNAME": "Admin",
  "PASSWORD": "admin123"
}
```

| Key | Description |
|-----|-------------|
| `BASE_URL` | The base URL of the application under test |
| `USERNAME` | Login username |
| `PASSWORD` | Login password |

---

## Running Tests

### Run all tests (Chrome, headed)
```bash
mvn test
```

### Run with a specific browser
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Run headless
```bash
mvn test -Dheadless=true
```

### Run headless on a specific browser
```bash
mvn test -Dbrowser=firefox -Dheadless=true
mvn test -Dbrowser=edge -Dheadless=true
```

### Run a specific tag
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

The `browser` and `headless` flags can also be set via environment variables (`BROWSER`, `HEADLESS`) — useful in Docker or CI environments where you cannot change the Maven command.

---

## CI/CD

A GitHub Actions workflow (`.github/workflows/maven.yml`) runs on every push and pull request to `main`:

1. Checks out the code and sets up Java 21 (Temurin)
2. Restores the Maven dependency cache to speed up builds
3. Runs the full test suite headless: `mvn test -Dbrowser=chrome -Dheadless=true`
4. Uploads the Cucumber HTML/JSON reports under the `cucumber-reports` artifact (always)
5. Uploads failure screenshots under `failure-screenshots` (only on failure)

The workflow also supports manual triggering (`workflow_dispatch`) with an optional `browser` input so you can run a one-off Firefox or Edge pipeline from the GitHub Actions UI.

---

## Test Scenarios

### `Login.feature`

```gherkin
Feature: OrangeHRM Login

  Scenario: Login using valid username and password
    Given the user is on the login page
    When the user enters valid credentials
    And the user clicks the login button
    Then the user is redirected to the Dashboard

  Scenario: User logs in and searches the directory by name
    Given the user is on the login page
    When the user enters valid credentials
    And the user clicks the login button
    And the user navigates to the Directory menu
    Then the user searches the directory using a generated name
```

---

## Architecture

### Page Object Model
`pages/LoginPage.java` and `pages/DirectoryPage.java` each extend `BasePage`. Locators are private static `By` fields, and every interaction is exposed as a named method. Step definitions instantiate the page object with `BrowserDriver.getDriver()` and call these methods directly — no raw `By` locators in step code.

### BasePage — Reusable Action Layer
All page interactions go through `BasePage`, which wraps every action with a `WebDriverWait` (30s timeout) to handle dynamic elements gracefully:

```java
click(By locator)
type(By locator, String text)
getText(By locator)
isVisible(By locator)
waitForVisibility(By locator)
waitForClickability(By locator)
```

### Locators (Deprecated)
The original `locators/` package (`LoginPage.java`, `DirectoryPage.java`) held static `By` fields only — no logic, no driver references. These are kept for reference but superseded by the `pages/` package.

### Hooks — Lifecycle & Screenshots on Failure
`utility/Hooks.java` manages the browser lifecycle via `@Before` / `@After`. On failure it:
- Attaches the screenshot inline to the **Cucumber HTML report**
- Saves a timestamped `.png` to `target/screenshots/`

### FakeDataUtil — Dynamic Test Data
Uses DataFaker to generate realistic random values at runtime, preventing hardcoded data and enabling more robust directory/search tests:
```java
FakeDataUtil.firstName()
FakeDataUtil.fullName()
FakeDataUtil.email()
FakeDataUtil.phoneNumber()
```

---

## Reports

After `mvn test`, timestamped reports are generated in `target/report/`:

```
target/
├── report/
│   ├── html/
│   │   └── report_01152025_0230PM.html   # Visual HTML report
│   └── json/
│       └── report_01152025_0230PM.json   # Machine-readable JSON report
└── screenshots/
    └── FailedScenario_01152025_023045.png # Failure screenshots
```

Open the `.html` file in any browser for a full visual breakdown of passed/failed scenarios, steps, and embedded screenshots.

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Add your `.feature` file under `src/test/java/features/`
4. Implement step definitions under `src/test/java/stepDefinition/`
5. Add page objects under `src/test/java/pages/`
6. Run `mvn test` to verify
7. Open a pull request

---

## Author

**Bryle Briones** — Senior QA Automation Engineer  
[LinkedIn](https://www.linkedin.com/in/bryle-briones-a24974167/) · [GitHub](https://github.com/Buraizuuu)
