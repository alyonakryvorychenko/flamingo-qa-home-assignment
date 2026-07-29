package com.flamingo.qa.pages.graphQL;

import com.flamingo.qa.config.ConfigProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public final class GraphQLClient {

    private RequestSpecification spec() {
        return given()
                .baseUri(ConfigProvider.get("graphql.url"))
                .contentType(ContentType.JSON);
    }

    public Response execute(String query, Map<String, Object> variables) {
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("query", query);
        requestBody.put("variables", variables);

        return spec()
                .body(requestBody)
                .when()
                .post();
    }
}
