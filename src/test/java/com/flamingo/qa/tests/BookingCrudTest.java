package com.flamingo.qa.tests;

import com.flamingo.qa.pages.api.AuthClient;
import com.flamingo.qa.pages.api.BookingClient;
import com.flamingo.qa.testdata.APITestData;
import com.flamingo.qa.models.AuthResponse;
import com.flamingo.qa.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.flamingo.qa.testdata.APITestData.VALID_PASSWORD;
import static com.flamingo.qa.testdata.APITestData.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
class BookingCrudTest {

    private final BookingClient bookingClient = new BookingClient();

    private static String token;

    @BeforeAll
    static void authenticate() {
        Response response = new AuthClient().getToken(VALID_USERNAME, VALID_PASSWORD);
        token = response.as(AuthResponse.class).getToken();
    }

    @Test
    @DisplayName("Create new booking with valid data")
    void createBooking_withValidData() {
        Booking booking = APITestData.defaultBooking();

        Response response = bookingClient.create(booking);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getInt("bookingid")).isPositive();

        Booking createdBooking = response.jsonPath().getObject("booking", Booking.class);
        assertThat(createdBooking).isEqualTo(booking);
    }

    @Test
    @DisplayName("Get booking by id returns matching data")
    void getBookingById_existingId() {
        Booking booking = APITestData.defaultBooking();
        int bookingId = bookingClient.create(booking).jsonPath().getInt("bookingid");

        Response response = bookingClient.getById(bookingId);

        assertThat(response.statusCode()).isEqualTo(200);

        Booking actualBooking = response.as(Booking.class);
        assertThat(actualBooking).isEqualTo(booking);
    }

    @Test
    @DisplayName("Update booking name, price and checkout date")
    void updateBooking_withValidToken() {
        Booking booking = APITestData.defaultBooking();
        int bookingId = bookingClient.create(booking).jsonPath().getInt("bookingid");

        Booking updatedBooking = booking.toBuilder()
                .firstname("James")
                .totalprice(250)
                .bookingdates(booking.getBookingdates().toBuilder()
                        .checkout("2026-09-15")
                        .build())
                .build();

        Response response = bookingClient.update(bookingId, updatedBooking, token);
        assertThat(response.statusCode()).isEqualTo(200);
        Booking actualBooking = response.as(Booking.class);
        assertThat(actualBooking).isEqualTo(updatedBooking);
    }

    @Test
    @DisplayName("Delete booking with valid token ")
    void deleteBooking_withValidToken() {
        Booking booking = APITestData.defaultBooking();
        int bookingId = bookingClient.create(booking).jsonPath().getInt("bookingid");

        Response response = bookingClient.delete(bookingId, token);
        assertThat(response.statusCode()).isEqualTo(201);

        Response getResponse = bookingClient.getById(bookingId);
        assertThat(getResponse.statusCode()).isEqualTo(404);
    }
}
