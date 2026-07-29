# QA Automation Test Suite

## Prerequisites
- Java 17
- Maven 3.9+
- No local Chrome install needed — Playwright downloads its own bundled Chromium on first run

## How to Run
```
# Run all tests
mvn clean test

# Run only API tests
mvn test -Dgroups="api"

# Run only GraphQL tests
mvn test -Dgroups="graphql"

# Run only UI tests
mvn test -Dgroups="ui"
```

## Test Strategy
Order: REST API first, then GraphQL, UI last (as recommended by the assignment), to guarantee minimum coverage at each stage.

- **REST API** — focus on positive + negative scenarios (create/get/update/delete, invalid data, missing token, non-existent ID), not just the "happy path".
- **GraphQL** — no separate models (POJOs): the request structure (query + variables) is fixed, so the body is built as a `Map<String, Object>` directly inside `GraphQLClient`.
- **UI** — Page Object Model: the page contains only locators and actions, all assertions live in the tests. Test data is generated dynamically (`UITestData.randomRecord()`), with no hardcoded values.
- **Synchronization** — only Playwright's built-in waiting, no `Thread.sleep`.

## Challenges & Solutions

- **Restful Booker: DELETE returns 201 Created, not 204.** Not a test bug — verified manually as the actual behavior of the public API and documented as-is.
- **GraphQL: fragility of public demo data.** Not every record has a full set of related entities (e.g. not every movie has a poster). Used `Assumptions.assumeTrue(...)` instead of a hard `assertThat` for the nested-fields/fragment test — if the data changes, the test shows as skipped rather than failed.
- **GraphQL: avoiding hardcoded IDs.** Entity IDs in the public demo database can change anytime. Tests fetch a real ID dynamically via a list query (`@BeforeAll`) instead of hardcoding one.
- **DemoQA Web Tables: no sorting functionality.** The assignment lists "Sorting validation" as required, but the actual page has no column sorting (headers aren't clickable, no sort controls exist). Verified manually — no test implemented, since there's no functionality to validate.
- **Web Tables search: dependency on table state.** The search test relies on DemoQA's default rows. To avoid interference from earlier tests (e.g. a deleted default row), the page is force-refreshed (`refresh()`) before searching to restore the table to its initial state.

## What I Would Add With More Time
- Add a retry mechanism for UI tests, since they depend on a public third-party demo site (DemoQA) that can occasionally be slow or flaky.
- Expand negative/edge-case coverage on the UI side (e.g. invalid email format, boundary values for age/salary) beyond the empty-form validation scenario.
- Add screenshots on success too (not just failure), attached to Allure, for easier visual review of what each test actually did.
