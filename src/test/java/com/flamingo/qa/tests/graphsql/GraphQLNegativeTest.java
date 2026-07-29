package com.flamingo.qa.tests.graphsql;

import com.flamingo.qa.pages.graphQL.GraphQLClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.flamingo.qa.testdata.GraphQLTestData.MOVIES_LIST_QUERY;
import static com.flamingo.qa.testdata.GraphQLTestData.MOVIE_BY_ID_QUERY;
import static com.flamingo.qa.testdata.GraphQLTestData.BROKEN_SYNTAX_QUERY;
import static com.flamingo.qa.testdata.GraphQLTestData.NONEXISTENT_FIELD_QUERY;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("graphql")
class GraphQLNegativeTest {

    private final GraphQLClient graphQLClient = new GraphQLClient();

    private static String movieId;

    @BeforeAll
    static void fetchMovieId() {
        Response response = new GraphQLClient().execute(MOVIES_LIST_QUERY, Map.of("first", 1));
        movieId = response.jsonPath().getString("data.movies[0].id");
    }

    @Test
    @DisplayName("Query by non-existent id movie returns 200 with null data and no errors")
    void queryMovieById_withNonExistentID() {
        Response response = graphQLClient.execute(MOVIE_BY_ID_QUERY, Map.of("id", "nonexistent000000000000"));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().<Object>get("data.movie")).isNull();
    }

    @Test
    @DisplayName("Query with syntax error returns 400 and an error message")
    void query_withSyntaxError() {
        Response response = graphQLClient.execute(BROKEN_SYNTAX_QUERY, null);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getString("errors[0].message"))
                .isNotBlank()
                .containsIgnoringCase("parse error");
    }

    @Test
    @DisplayName("Query with non-existent field returns 400 validation error")
    void queryMovie_withNonExistentField() {
        Response response = graphQLClient.execute(NONEXISTENT_FIELD_QUERY, Map.of("id", movieId));

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getString("errors[0].message")).contains("thisFieldDoesNotExist");
    }
}
