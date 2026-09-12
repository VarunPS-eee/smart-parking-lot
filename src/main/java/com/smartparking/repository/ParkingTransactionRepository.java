package com.smartparking.repository;

import com.smartparking.ParkingTransaction;
import com.smartparking.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingTransactionRepository extends JpaRepository<ParkingTransaction, Long> {
    Optional<ParkingTransaction> findTopByVehicleOrderByCheckInTimeDesc(Vehicle vehicle);
}
