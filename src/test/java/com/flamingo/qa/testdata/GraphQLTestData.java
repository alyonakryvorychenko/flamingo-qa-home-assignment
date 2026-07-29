package com.flamingo.qa.testdata;

public final class GraphQLTestData {

    private GraphQLTestData() {
    }

    public static final String MOVIES_LIST_QUERY = """
            query MoviesList($first: Int!) {
              movies(first: $first) {
                id
                title
                slug
              }
            }
            """;

    public static final String MOVIE_BY_ID_QUERY = """
            query MovieById($id: ID!) {
              movie(where: { id: $id }) {
                id
                title
                slug
              }
            }
            """;

    public static final String MOVIE_WITH_FRAGMENT_QUERY = """
            query MovieWithFragment($id: ID!) {
              movie(where: { id: $id }) {
                ...MovieCore
                moviePoster {
                  url
                  publishedBy {
                    id
                    name
                  }
                }
              }
            }
            fragment MovieCore on Movie {
              id
              title
              slug
            }
            """;

    public static final String BROKEN_SYNTAX_QUERY = """
            query BrokenQuery {
              movies( {
                title
              }
            }
            """;

    public static final String NONEXISTENT_FIELD_QUERY = """
            query MovieBadField($id: ID!) {
              movie(where: { id: $id }) {
                id
                thisFieldDoesNotExist
              }
            }
            """;
}
