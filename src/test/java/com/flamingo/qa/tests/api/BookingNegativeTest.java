package com.flamingo.qa.tests.api;

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
class BookingNegativeTest {

    private final BookingClient bookingClient = new BookingClient();

    private static String token;

    @BeforeAll
    static void authenticate() {
        Response response = new AuthClient().getToken(VALID_USERNAME, VALID_PASSWORD);
        token = response.as(AuthResponse.class).getToken();
    }

    @Test
    @DisplayName("Create booking without required fields returns error")
    void createBooking_withMissingRequiredFields_returnsError() {
        // lastname and bookingdates deliberately omitted (both required by the API)
        Booking incompleteBooking = Booking.builder()
                .firstname("Jim")
                .totalprice(111)
                .depositpaid(true)
                .build();

        Response response = bookingClient.create(incompleteBooking);

        assertThat(response.statusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Update booking with invalid token")
    void updateBooking_withInvalidToken() {
        Booking booking = APITestData.defaultBooking();
        int bookingId = bookingClient.create(booking).jsonPath().getInt("bookingid");

        Response response = bookingClient.update(bookingId, booking, "invalid-token-12345");

        assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("Delete booking with invalid token")
    void deleteBooking_withInvalidToken() {
        Booking booking = APITestData.defaultBooking();
        int bookingId = bookingClient.create(booking).jsonPath().getInt("bookingid");

        Response response = bookingClient.delete(bookingId, "invalid-token-12345");

        assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("Delete booking with non-existent id")
    void deleteBooking_withNonExistentId() {
        Response response = bookingClient.delete(Integer.MAX_VALUE, token);

        assertThat(response.statusCode()).isEqualTo(405);
    }

    @Test
    @DisplayName("Get booking by non-existent id")
    void getBookingById_nonExistentBookingId() {
        Response response = bookingClient.getById(Integer.MAX_VALUE);
        assertThat(response.statusCode()).isEqualTo(404);
    }
}
