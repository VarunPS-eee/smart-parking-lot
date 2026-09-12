package com.smartparking.controller;

import com.smartparking.ParkingFeeCalculator;
import com.smartparking.ParkingSpot;
import com.smartparking.ParkingSpotAllocationService;
import com.smartparking.TransactionStatus;
import com.smartparking.Vehicle;
import com.smartparking.VehicleType;
import com.smartparking.repository.ParkingSpotRepository;
import com.smartparking.repository.ParkingTransactionRepository;
import com.smartparking.repository.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ParkingController {

    private final ParkingSpotRepository parkingSpotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingTransactionRepository parkingTransactionRepository;

    public ParkingController(ParkingSpotRepository parkingSpotRepository,
                            VehicleRepository vehicleRepository,
                            ParkingTransactionRepository parkingTransactionRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingTransactionRepository = parkingTransactionRepository;
    }

    @PostMapping("/vehicles/checkin")
    public ResponseEntity<?> checkIn(@RequestParam String licensePlate,
                                    @RequestParam VehicleType vehicleType) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseGet(() -> vehicleRepository.save(new Vehicle(licensePlate, vehicleType)));

        ParkingSpot spot = parkingSpotRepository.findFirstByVehicleTypeAndAvailableTrue(vehicleType)
                .orElseThrow(() -> new IllegalStateException("No available spot for this vehicle type"));

        spot.setAvailable(false);
        parkingSpotRepository.save(spot);

        var transaction = new com.smartparking.ParkingTransaction(
                vehicle,
                spot,
                LocalDateTime.now(),
                null,
                TransactionStatus.ACTIVE,
                0
        );

        parkingTransactionRepository.save(transaction);

        return ResponseEntity.ok("Vehicle checked in. Spot assigned: " + spot.getSpotNumber());
    }

    @PostMapping("/vehicles/checkout")
    public ResponseEntity<?> checkOut(@RequestParam String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new IllegalStateException("Vehicle not found"));

        var transaction = parkingTransactionRepository.findTopByVehicleOrderByCheckInTimeDesc(vehicle)
                .orElseThrow(() -> new IllegalStateException("No active transaction found"));

        transaction.setCheckOutTime(LocalDateTime.now());
        long minutes = Duration.between(transaction.getCheckInTime(), transaction.getCheckOutTime()).toMinutes();
        int fee = ParkingFeeCalculator.calculateFee(vehicle.getVehicleType(), (int) minutes);
        transaction.setFee(fee);
        transaction.setStatus(TransactionStatus.COMPLETED);

        ParkingSpot spot = transaction.getParkingSpot();
        spot.setAvailable(true);
        parkingSpotRepository.save(spot);
        parkingTransactionRepository.save(transaction);

        return ResponseEntity.ok("Vehicle checked out. Fee: " + fee);
    }

    @GetMapping("/spots")
    public List<ParkingSpot> getAllSpots() {
        return parkingSpotRepository.findAll();
    }
}
