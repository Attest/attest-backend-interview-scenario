package com.askattest.interview.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RespondentEarningTest {

    @Test
    void newRespondentEarning_HasZeroValues() {
        // When
        RespondentEarning earning = new RespondentEarning(1);

        // Then
        assertEquals(0, earning.amount());
        assertEquals(0, earning.answers());
    }

    @Test
    void increment_UpdatesBothFields() {
        // Given
        RespondentEarning earning = new RespondentEarning(1);

        // When
        earning.increment(100);
        earning.increment(200);

        // Then
        assertEquals(300, earning.amount());
        assertEquals(2, earning.answers());
    }

    @Test
    void increment_WithZeroPayout() {
        // Given
        RespondentEarning earning = new RespondentEarning(1);

        // When
        earning.increment(0);

        // Then
        assertEquals(0, earning.amount());
        assertEquals(1, earning.answers());
    }

    @Test
    void toString_ReturnsFormattedString() {
        // Given
        RespondentEarning earning = new RespondentEarning(5);
        earning.increment(150);
        earning.increment(250);

        // When
        String result = earning.toString();

        // Then
        assertEquals("[Respondent: 5 earned 400 pences for their 2 answers.]", result);
    }
}