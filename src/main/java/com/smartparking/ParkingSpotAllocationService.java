package com.smartparking;

import java.util.List;
import java.util.Optional;

public class ParkingSpotAllocationService {

    private final List<ParkingSpot> parkingSpots;

    public ParkingSpotAllocationService(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public ParkingSpot allocateSpotFor(VehicleType vehicleType) {
        Optional<ParkingSpot> match = parkingSpots.stream()
                .filter(ParkingSpot::isAvailable)
                .filter(spot -> spot.getVehicleType() == vehicleType)
                .findFirst();

        if (match.isEmpty()) {
            throw new IllegalStateException("No available spot for vehicle type: " + vehicleType);
        }

        ParkingSpot spot = match.get();
        spot.setAvailable(false);
        return spot;
    }
}
