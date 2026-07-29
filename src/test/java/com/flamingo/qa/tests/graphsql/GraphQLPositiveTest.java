package com.flamingo.qa.tests.graphsql;

import com.flamingo.qa.pages.graphQL.GraphQLClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.flamingo.qa.testdata.GraphQLTestData.MOVIES_LIST_QUERY;
import static com.flamingo.qa.testdata.GraphQLTestData.MOVIE_BY_ID_QUERY;
import static com.flamingo.qa.testdata.GraphQLTestData.MOVIE_WITH_FRAGMENT_QUERY;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("graphql")
class GraphQLPositiveTest {

    // Manually verified to have both a moviePoster and a publishedBy.
    private static final String MOVIE_ID_WITH_POSTER = "clq16l6aq0pue0blhwfgmhor1";

    private final GraphQLClient graphQLClient = new GraphQLClient();

    private static String movieId;

    @BeforeAll
    static void fetchMovieId() {
        Response response = new GraphQLClient().execute(MOVIES_LIST_QUERY, Map.of("first", 1));
        movieId = response.jsonPath().getString("data.movies[0].id");
    }

    @Test
    @DisplayName("List query with pagination limit returns at most the requested number of results")
    void queryMoviesList_withPaginationLimit() {
        Response response = graphQLClient.execute(MOVIES_LIST_QUERY, Map.of("first", 5));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getList("data.movies")).hasSizeLessThanOrEqualTo(5);
    }

    @Test
    @DisplayName("Query single entity by ID using GraphQL variables (not string interpolation) returns matching movie")
    void queryMovieById_usingVariables_returnsMatchingMovie() {
        Response response = graphQLClient.execute(MOVIE_BY_ID_QUERY, Map.of("id", movieId));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("data.movie.id")).isEqualTo(movieId);
    }

    @Test
    @DisplayName("Query with fragment and nested fields returns publisher name")
    void queryMovie_withFragmentAndNestedFields_returnsPublisherName() {
        // Uses a hardcoded id instead of the dynamic movieId from @BeforeAll: moviePoster is
        // null for many movies in the demo data, so a randomly picked movie would often fail
        // to exercise the nested-fields path this test is meant to cover.
        Response response = graphQLClient.execute(MOVIE_WITH_FRAGMENT_QUERY, Map.of("id", MOVIE_ID_WITH_POSTER));

        assertThat(response.statusCode()).isEqualTo(200);

        String publisherName = response.jsonPath().getString("data.movie.moviePoster.publishedBy.name");
        Assumptions.assumeTrue(publisherName != null, "Movie " + MOVIE_ID_WITH_POSTER + " no longer has a poster — demo data may have changed");

        assertThat(publisherName).isNotBlank();
    }
}
