# Hybrid UI + API Automation Framework

![CI](https://github.com/parthverma22/hybrid-automation-framework/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-blue)
![Selenium](https://img.shields.io/badge/Selenium-4.18-green)
![TestNG](https://img.shields.io/badge/TestNG-7.9-orange)
![REST%20Assured](https://img.shields.io/badge/REST%20Assured-5.4-blueviolet)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow)

A production-grade test automation framework that combines **UI automation** (SauceDemo) and **REST API testing** (reqres.in + Swagger Petstore) in a single, well-structured Maven project.

Designed to demonstrate SDET-2 level skills: framework design, parallel execution, data-driven testing, and CI/CD pipeline integration.

---

## Architecture

```
┌──────────────────────────────────────────────────────────────────────┐
│                   HYBRID AUTOMATION FRAMEWORK                        │
│               Java 17 · Maven · TestNG · GitHub Actions              │
└─────────────────────────┬────────────────────────────────────────────┘
                          │
          ┌───────────────┴────────────────┐
          │                                │
   ┌──────▼───────┐                ┌───────▼──────┐
   │   UI Layer   │                │   API Layer  │
   │  (Selenium 4)│                │(REST Assured)│
   └──────┬───────┘                └───────┬──────┘
          │                                │
   ┌──────▼───────┐                ┌───────▼──────┐
   │  Page Object │                │  RestClient  │
   │    Model     │                │   Wrapper    │
   │(LoginPage,   │                │(get/post/put/│
   │InventoryPage)│                │ patch/delete)│
   └──────┬───────┘                └───────┬──────┘
          │                                │
          └──────────────┬─────────────────┘
                         │
              ┌──────────▼──────────┐
              │   BaseTest /        │
              │   BaseApiTest       │
              │   (TestNG lifecycle)│
              └──────────┬──────────┘
                         │
       ┌─────────────────┼─────────────────┐
       │                 │                 │
┌──────▼──────┐  ┌───────▼──────┐  ┌──────▼──────┐
│   Config    │  │   Reports    │  │  Test Data  │
│  Reader     │  │  ExtentRpts  │  │ Apache POI  │
│ (Properties)│  │  + Allure    │  │  (.xlsx)    │
└─────────────┘  └──────────────┘  └─────────────┘
                         │
              ┌──────────▼──────────┐
              │   GitHub Actions    │
              │  CI/CD Pipeline     │
              │  (Parallel jobs)    │
              └─────────────────────┘
```

---

## Tech Stack

| Layer              | Technology                     | Version |
|--------------------|-------------------------------|----------|
| Language           | Java                          | 17       |
| Build Tool         | Maven                         | 3.9+     |
| UI Automation      | Selenium WebDriver            | 4.18     |
| Driver Management  | WebDriverManager              | 5.8      |
| API Automation     | REST Assured                  | 5.4      |
| Test Framework     | TestNG                        | 7.9      |
| Reporting          | ExtentReports + Allure        | 5.1 / 2.25 |
| Data-Driven        | Apache POI (Excel .xlsx)      | 5.2.5    |
| Serialisation      | Jackson                       | 2.16     |
| Logging            | Log4j 2                       | 2.22     |
| CI/CD              | GitHub Actions                | —        |

---

## Project Structure

```
hybrid-automation-framework/
├── .github/workflows/
│   └── ci.yml                          # 3-job CI pipeline (API → UI → Parallel+Report)
├── src/
│   ├── main/java/com/hybrid/
│   │   ├── config/
│   │   │   └── ConfigReader.java       # Singleton properties loader
│   │   ├── constants/
│   │   │   └── FrameworkConstants.java # Paths and timeout values
│   │   ├── driver/
│   │   │   ├── DriverFactory.java      # Creates Chrome/Firefox instances
│   │   │   └── DriverManager.java      # ThreadLocal<WebDriver> for parallel safety
│   │   ├── pages/                      # Page Object Model
│   │   │   ├── LoginPage.java
│   │   │   └── InventoryPage.java
│   │   ├── api/
│   │   │   └── RestClient.java         # REST Assured wrapper
│   │   ├── reports/
│   │   │   └── ExtentManager.java      # ThreadLocal ExtentTest + singleton ExtentReports
│   │   └── utils/
│   │       ├── ExcelUtils.java         # Apache POI — reads & auto-generates .xlsx
│   │       └── WaitUtils.java          # Explicit wait helpers
│   └── test/java/com/hybrid/
│       ├── base/
│       │   ├── BaseTest.java           # UI driver lifecycle
│       │   └── BaseApiTest.java        # API RestClient setup
│       ├── listeners/
│       │   └── TestListener.java       # ITestListener → ExtentReports
│       ├── ui/
│       │   ├── LoginTest.java          # 3 tests (data-driven + valid + invalid)
│       │   └── InventoryTest.java      # 4 tests (item count, cart)
│       └── api/
│           ├── UserApiTest.java        # 6 tests on reqres.in
│           └── PetApiTest.java         # 4 tests on Swagger Petstore
├── src/test/resources/
│   ├── config.properties
│   ├── testng.xml                      # Sequential full suite
│   ├── testng-parallel.xml             # Parallel (thread-count=4)
│   ├── testng-ui.xml
│   └── testng-api.xml
└── pom.xml
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- Google Chrome (for UI tests)

### Clone & Install

```bash
git clone https://github.com/parthverma22/hybrid-automation-framework.git
cd hybrid-automation-framework
mvn clean install -DskipTests
```

---

## Running Tests

| Command | What it runs |
|---------|-------------|
| `mvn test` | Full suite, sequential |
| `mvn test -DsuiteFile=src/test/resources/testng-parallel.xml` | Parallel (4 threads) |
| `mvn test -DsuiteFile=src/test/resources/testng-api.xml` | API tests only |
| `mvn test -DsuiteFile=src/test/resources/testng-ui.xml` | UI tests only |
| `mvn test -Dheadless=true` | Headless Chrome |
| `mvn test -Dbrowser=firefox` | Firefox |
| `mvn allure:report` | Generate Allure HTML report |

### Headless parallel run

```bash
mvn test \
  -DsuiteFile=src/test/resources/testng-parallel.xml \
  -Dbrowser=chrome \
  -Dheadless=true
```

---

## Reports

**ExtentReports** — generated automatically:
```
test-output/extent-reports/Report_<timestamp>.html
```

**Allure**:
```bash
mvn allure:report
# target/site/allure-maven-plugin/index.html
```

---

## CI/CD Pipeline

Three-job GitHub Actions pipeline:

```
push / PR / schedule (weekdays 06:00 UTC)
          │
    ┌─────┴─────┐
    │           │
 api-tests   ui-tests        ← run in parallel
    │           │
    └─────┬─────┘
          │
 parallel-full-suite + Allure report
```

---

## Test Coverage

| Module | Tests | Target |
|--------|-------|--------|
| UI – Login | 5 (incl. 3 Excel-driven rows) | saucedemo.com |
| UI – Inventory | 4 | saucedemo.com |
| API – Users | 6 (GET list/id/404, POST, PUT, PATCH, DELETE) | reqres.in |
| API – Petstore | 4 (POST, GET, findByStatus, DELETE) | petstore.swagger.io |
| **Total** | **17** | |

---

## Key Design Decisions

| Decision | Reason |
|----------|--------|
| `ThreadLocal<WebDriver>` | Safe parallel browser sessions |
| `WebDriverManager` | Zero-config — no manual ChromeDriver maintenance |
| `@BeforeSuite` generates Excel | Self-contained; no binary blobs in version control |
| `BaseTest` vs `BaseApiTest` | API tests never spin up a browser |
| Fluent Page Object methods | Chainable, readable: `.enterUsername().enterPassword().clickLogin()` |
| `-Dbrowser` / `-Dheadless` flags | Runtime overrides without code changes |

---

## Author

**Parth Verma** — SDET

> Scaffolded with the assistance of [Claude](https://claude.ai) (Anthropic AI).
