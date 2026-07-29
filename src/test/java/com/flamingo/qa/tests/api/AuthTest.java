package com.flamingo.qa.tests.api;

import com.flamingo.qa.pages.api.AuthClient;
import com.flamingo.qa.models.AuthResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.flamingo.qa.testdata.APITestData.VALID_PASSWORD;
import static com.flamingo.qa.testdata.APITestData.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
class AuthTest {

    private final AuthClient authClient = new AuthClient();

    @Test
    @DisplayName("Valid credentials return an auth token")
    void getToken_withValidCredentials_returnsToken() {
        Response response = authClient.getToken(VALID_USERNAME, VALID_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(200);

        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(authResponse.getToken()).isNotBlank();
    }

    @Test
    @DisplayName("Invalid credentials return a reason instead of a token")
    void getToken_withInvalidCredentials_returnsReason() {
        Response response = authClient.getToken(VALID_USERNAME, "");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("token")).isNull();
        assertThat(response.jsonPath().getString("reason")).isEqualTo("Bad credentials");
    }
}
