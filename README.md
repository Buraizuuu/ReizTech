# Selenium BDD Framework

A Behavior-Driven Development (BDD) test automation framework built with **Selenium 4**, **Cucumber 7**, and **JUnit 4** in Java. Designed to be readable, maintainable, and ready for CI/CD pipelines.

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

## Project Structure

```
Selenium-BDD/
├── pom.xml
└── src/
    └── test/
        └── java/
            ├── features/
            │   └── Login.feature          # Gherkin test scenarios
            ├── locators/
            │   ├── LoginPage.java         # Login page element locators
            │   └── DirectoryPage.java     # Directory page element locators
            ├── resources/
            │   └── config/
            │       └── config.json        # Environment configuration
            ├── runner/
            │   └── TestRunner.java        # Cucumber JUnit runner
            ├── stepDefinition/
            │   ├── Hooks.java             # Before/After hooks + screenshots
            │   ├── LoginPageSteps.java    # Login step implementations
            │   └── DirectoryPageSteps.java# Directory step implementations
            └── utility/
                ├── BasePage.java          # Reusable WebDriver action wrappers
                ├── BrowserDriver.java     # WebDriver factory (Chrome/Firefox/Edge)
                ├── ConfigReader.java      # Reads config.json at runtime
                └── FakeDataUtil.java      # Random test data via DataFaker
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

### Run all tests
```bash
mvn test
```

### Run with a specific browser
Open `src/test/java/utility/BrowserDriver.java` and change the `BROWSER` constant:
```java
private static final String BROWSER = "chrome";  // "firefox" or "edge"
```

### Run headless
In `BrowserDriver.java`, toggle the headless flag:
```java
private static final boolean isHeadless = true;
```

### Run a specific tag
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

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

### Page Locators
Locator classes (e.g. `LoginPage.java`, `DirectoryPage.java`) hold static `By` fields only — no logic, no driver references. This keeps locator maintenance separate from behavior.

### Hooks — Screenshots on Failure
`Hooks.java` automatically captures a screenshot on any failing scenario:
- Attaches it inline to the **Cucumber HTML report**
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
5. Add locators under `src/test/java/locators/`
6. Run `mvn test` to verify
7. Open a pull request

---

## Author

**Bryle Briones** — Senior QA Automation Engineer  
[LinkedIn](https://www.linkedin.com/in/bryle-briones-a24974167/) · [GitHub](https://github.com/Buraizuuu)
