package com.flamingo.qa.clients;

import com.flamingo.qa.config.ConfigProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.restassured.RestAssured.given;

public final class GraphQLClient {

    private RequestSpecification spec() {
        return given()
                .baseUri(ConfigProvider.get("graphql.url"))
                .contentType(ContentType.JSON);
    }

    public Response execute(String query, Map<String, Object> variables) {
        GraphQLRequest requestBody = GraphQLRequest.builder()
                .query(query)
                .variables(variables)
                .build();

        return spec()
                .body(requestBody)
                .when()
                .post();
    }

    public Response executeFromFile(String fileName, Map<String, Object> variables) {
        return execute(readQuery(fileName), variables);
    }

    private String readQuery(String fileName) {
        String resourcePath = "graphql/" + fileName;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found on classpath: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read GraphQL query: " + resourcePath, e);
        }
    }
}
