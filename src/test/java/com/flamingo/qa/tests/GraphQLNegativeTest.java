package com.flamingo.qa.tests;

import com.flamingo.qa.clients.GraphQLClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("graphql")
class GraphQLNegativeTest {

    private final GraphQLClient graphQLClient = new GraphQLClient();

    @Test
    @DisplayName("Query by non-existent id returns empty result")
    void query_withNonExistentId_returnsEmptyResult() {
        // TODO: implement
    }

    @Test
    @DisplayName("Malformed query returns GraphQL syntax error")
    void malformedQuery_returnsSyntaxError() {
        // TODO: implement
    }

    @Test
    @DisplayName("Query with non-existent field returns validation error")
    void query_withNonExistentField_returnsValidationError() {
        // TODO: implement
    }
}
