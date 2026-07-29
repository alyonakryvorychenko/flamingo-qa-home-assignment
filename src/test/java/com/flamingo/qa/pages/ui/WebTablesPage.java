package com.flamingo.qa.pages.ui;

import com.flamingo.qa.testdata.UITestData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WebTablesPage {

    private static final String[] COLUMNS =
            {"firstName", "lastName", "age", "email", "salary", "department"};

    private final Page page;

    private final Locator addButton;
    private final Locator searchBox;
    private final Locator tableRows;
    private final Locator submitButton;

    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator ageInput;
    private final Locator salaryInput;
    private final Locator departmentInput;

    public WebTablesPage(Page page) {
        this.page = page;

        this.addButton = page.locator("#addNewRecordButton");
        this.searchBox = page.locator("#searchBox");
        this.tableRows = page.locator("table tbody tr");
        this.submitButton = page.locator("#submit");

        this.firstNameInput = page.locator("#firstName");
        this.lastNameInput = page.locator("#lastName");
        this.emailInput = page.locator("#userEmail");
        this.ageInput = page.locator("#age");
        this.salaryInput = page.locator("#salary");
        this.departmentInput = page.locator("#department");
    }

    public void open() {
        page.navigate(UITestData.BASE_URL);
    }

    public void refresh() {
        page.navigate(UITestData.BASE_URL);
    }

    public void clickAdd() {
        addButton.click();
        firstNameInput.waitFor();
    }

    public void fillForm(String firstName, String lastName, String email, String age, String salary, String department) {
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        emailInput.fill(email);
        ageInput.fill(age);
        salaryInput.fill(salary);
        departmentInput.fill(department);
    }

    public void fillFirstName(String firstName) {
        firstNameInput.fill(firstName);
    }

    public void clickSubmit() {
        submitButton.click();
    }

    public List<Map<String, String>> getAllRows() {
        List<Map<String, String>> rows = new ArrayList<>();

        for (int i = 0; i < tableRows.count(); i++) {
            List<String> cells = tableRows.nth(i).locator("td").allTextContents();

            boolean isPlaceholderRow = cells.get(0).isBlank() && cells.get(3).isBlank();
            if (isPlaceholderRow) {
                continue;
            }

            Map<String, String> row = new LinkedHashMap<>();
            for (int c = 0; c < COLUMNS.length; c++) {
                row.put(COLUMNS[c], cells.get(c).trim());
            }
            rows.add(row);
        }

        return rows;
    }

    public int getRowCount() {
        return getAllRows().size();
    }

    public Map<String, String> getRow(int index) {
        return getAllRows().get(index);
    }

    public void clickEditByRowIndex(int index) {
        tableRows.nth(index).locator("[id^='edit-record-']").click();
        firstNameInput.waitFor();
    }

    public void clickDeleteByRowIndex(int index) {
        tableRows.nth(index).locator("[id^='delete-record-']").click();
    }

    public void search(String query) {
        searchBox.fill(query);
    }

    private boolean isFieldInvalid(String fieldLocator) {
        return (Boolean) page.locator(fieldLocator).evaluate(
                "el => el.matches(':invalid') || getComputedStyle(el).borderColor === 'rgb(220, 53, 69)'");
    }

    public boolean areAllRequiredFieldsInvalid() {
        return isFieldInvalid("#firstName")
                && isFieldInvalid("#lastName")
                && isFieldInvalid("#userEmail")
                && isFieldInvalid("#age")
                && isFieldInvalid("#salary")
                && isFieldInvalid("#department");
    }

    public boolean validateNewTableRecord(Map<String, String> expected) {
        Optional<Map<String, String>> actualRow = getAllRows().stream()
                .filter(row -> expected.get("email").equals(row.get("email")))
                .findFirst();

        return actualRow.isPresent() && actualRow.get().equals(expected);
    }
}
