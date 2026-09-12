package com.smartparking;

public final class ParkingFeeCalculator {

    private ParkingFeeCalculator() {
    }

    public static int calculateFee(VehicleType vehicleType, int minutesParked) {
        if (minutesParked <= 0) {
            return 0;
        }

        int chargedHours = (int) Math.floor(minutesParked / 60.0);
        if (chargedHours == 0) {
            chargedHours = 1;
        }

        int hourlyRate = switch (vehicleType) {
            case MOTORCYCLE -> 40;
            case CAR -> 80;
            case BUS -> 120;
        };

        return chargedHours * hourlyRate;
    }
}
