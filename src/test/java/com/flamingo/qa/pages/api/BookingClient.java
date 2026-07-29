package com.flamingo.qa.pages.api;

import com.flamingo.qa.config.ConfigProvider;
import com.flamingo.qa.models.Booking;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public final class BookingClient {

    private RequestSpecification spec() {
        return given()
                .baseUri(ConfigProvider.get("api.baseUrl"))
                .contentType(ContentType.JSON);
    }

    public Response create(Booking booking) {
        return spec()
                .body(booking)
                .when()
                .post("/booking");
    }

    public Response getById(int id) {
        return spec()
                .when()
                .get("/booking/{id}", id);
    }

    public Response update(int id, Booking booking, String token) {
        return spec()
                .cookie("token", token)
                .body(booking)
                .when()
                .put("/booking/{id}", id);
    }

    public Response delete(int id, String token) {
        return spec()
                .cookie("token", token)
                .when()
                .delete("/booking/{id}", id);
    }
}
