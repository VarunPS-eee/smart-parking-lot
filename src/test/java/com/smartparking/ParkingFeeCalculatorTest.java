package com.smartparking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingFeeCalculatorTest {

    @Test
    void shouldCalculateFeeForCarForTwoHours() {
        assertEquals(160, ParkingFeeCalculator.calculateFee(VehicleType.CAR, 120));
    }

    @Test
    void shouldRoundUpDurationToNearestHour() {
        assertEquals(80, ParkingFeeCalculator.calculateFee(VehicleType.CAR, 61));
    }

    @Test
    void shouldCalculateFeeForMotorcycle() {
        assertEquals(80, ParkingFeeCalculator.calculateFee(VehicleType.MOTORCYCLE, 120));
    }
}
