package com.flamingo.qa.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookingDates {

    /** ISO date, format yyyy-MM-dd. */
    private String checkin;

    /** ISO date, format yyyy-MM-dd. */
    private String checkout;
}
