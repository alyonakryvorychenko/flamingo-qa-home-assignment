package com.flamingo.qa.testdata;

import com.flamingo.qa.models.Booking;
import com.flamingo.qa.models.BookingDates;

public final class APITestData {

    private APITestData() {
    }

    public static final String VALID_USERNAME = "admin";
    public static final String VALID_PASSWORD = "password123";

    public static Booking defaultBooking() {
        return Booking.builder()
                .firstname("Jim")
                .lastname("Brown")
                .totalprice(111)
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin("2026-08-15")
                        .checkout("2026-08-21")
                        .build())
                .additionalneeds("Breakfast")
                .build();
    }
}
