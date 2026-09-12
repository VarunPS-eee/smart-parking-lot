package com.smartparking;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingSpotAllocationServiceTest {

    @Test
    void shouldAllocateCarSpotToCar() {
        ParkingSpotAllocationService service = new ParkingSpotAllocationService(List.of(
                new ParkingSpot(1L, 1, "A1", VehicleType.MOTORCYCLE, true),
                new ParkingSpot(2L, 1, "A2", VehicleType.CAR, true),
                new ParkingSpot(3L, 1, "A3", VehicleType.BUS, true)
        ));

        ParkingSpot spot = service.allocateSpotFor(VehicleType.CAR);

        assertEquals("A2", spot.getSpotNumber());
    }

    @Test
    void shouldAllocateBusSpotToBus() {
        ParkingSpotAllocationService service = new ParkingSpotAllocationService(List.of(
                new ParkingSpot(1L, 1, "A1", VehicleType.CAR, true),
                new ParkingSpot(2L, 1, "A2", VehicleType.BUS, true)
        ));

        ParkingSpot spot = service.allocateSpotFor(VehicleType.BUS);

        assertEquals("A2", spot.getSpotNumber());
    }
}
