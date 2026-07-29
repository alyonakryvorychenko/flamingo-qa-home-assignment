package com.flamingo.qa.clients;

import com.flamingo.qa.config.ConfigProvider;
import com.flamingo.qa.models.AuthRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public final class AuthClient {

    private RequestSpecification spec() {
        return given()
                .baseUri(ConfigProvider.get("api.baseUrl"))
                .contentType(ContentType.JSON);
    }

    public Response getToken(String username, String password) {
        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();

        return spec()
                .body(authRequest)
                .when()
                .post("/auth");
    }
}
