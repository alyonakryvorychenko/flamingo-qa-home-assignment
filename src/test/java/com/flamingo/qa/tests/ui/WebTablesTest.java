package com.flamingo.qa.tests.ui;

import com.flamingo.qa.pages.ui.WebTablesPage;
import com.flamingo.qa.testdata.UITestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
class WebTablesTest extends BaseUiTest {

    private WebTablesPage webTablesPage;

    @BeforeEach
    void openWebTablesPage() {
        webTablesPage = new WebTablesPage(page);
        webTablesPage.open();
    }

    @Test
    @DisplayName("Adding a new record with valid data makes it appear in the table")
    void addNewRecord_withValidData_appearsInTable() {
        Map<String, String> record = UITestData.randomRecord();

        webTablesPage.clickAdd();
        webTablesPage.fillForm(record.get("firstName"), record.get("lastName"), record.get("email"),
                record.get("age"), record.get("salary"), record.get("department"));
        webTablesPage.clickSubmit();

        assertThat(webTablesPage.validateNewTableRecord(record)).isTrue();
    }

    @Test
    @DisplayName("Update record firstName and verify before/after values in table")
    void editRecord_updatesFirstName_reflectsChangeInTable() {
        String firstNameUpdateValue = "Updated" + System.currentTimeMillis();

        webTablesPage.clickEditByRowIndex(0);
        webTablesPage.fillFirstName(firstNameUpdateValue);
        webTablesPage.clickSubmit();

        String actualFirstName = webTablesPage.getAllRows().get(0).get("firstName");

        assertThat(actualFirstName).isEqualTo(firstNameUpdateValue);
    }

    @Test
    @DisplayName("Deleting a record removes it from the table and decreases the row count")
    void deleteRecord_removesFromTable_sizeDecreases() {
        int rowCountBefore = webTablesPage.getRowCount();
        String deletedEmail = webTablesPage.getRow(0).get("email");

        webTablesPage.clickDeleteByRowIndex(0);

        int rowCountAfter = webTablesPage.getRowCount();

        assertThat(rowCountAfter).isEqualTo(rowCountBefore - 1);
        assertThat(webTablesPage.getAllRows()).noneMatch(row -> deletedEmail.equals(row.get("email")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Cierra", "cierra@example.com", "Insurance"})
    @DisplayName("Searching the table returns only rows matching the query")
    void searchTable_withGivenQuery_returnsMatchingRows(String query) {
        webTablesPage.refresh(); // ensure default 3 rows are present, in case previous tests modified the table
        webTablesPage.search(query);
        List<Map<String, String>> rows = webTablesPage.getAllRows();
        assertThat(rows).allSatisfy(row -> assertThat(row.values()).anySatisfy(value -> assertThat(value).containsIgnoringCase(query)));
    }

    @Test
    @DisplayName("Submitting an empty form shows validation errors on all required fields")
    void submitEmptyForm_showsValidationOnAllFields() {
        webTablesPage.clickAdd();
        webTablesPage.clickSubmit();

        assertThat(webTablesPage.areAllRequiredFieldsInvalid()).isTrue();
    }
}
