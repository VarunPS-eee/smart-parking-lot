package com.smartparking.repository;

import com.smartparking.ParkingSpot;
import com.smartparking.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findFirstByVehicleTypeAndAvailableTrue(VehicleType vehicleType);
}
