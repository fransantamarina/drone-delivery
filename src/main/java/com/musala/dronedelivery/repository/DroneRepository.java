package com.musala.dronedelivery.repository;

import com.musala.dronedelivery.entity.Drone;
import com.musala.dronedelivery.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    boolean existsBySerialNumber(String serialNumber);

    //Todo: refactor below to avoid hardcoded value 25 and IDLE
    @Query("SELECT d FROM Drone d WHERE d.state = :state AND d.batteryCapacity >= :batteryCapacity")
    List<Drone> findAvailable(DroneState state, int batteryCapacity);

    Drone findBySerialNumber(String serialNumber);

    //TODO: refactor findAvailable to findBySerialNumberAndStateAndBatteryCapacityGreaterThan
    @Query("SELECT d FROM Drone d WHERE d.serialNumber = :serialNumber AND d.state = 'IDLE' AND d.batteryCapacity >= 25")
    Optional<Drone> findIfAvailable(String serialNumber);

    Optional<Drone> findBySerialNumberAndStateAndBatteryCapacityGreaterThanEqual(String serialNumber, DroneState state, int minBatteryLevel);
}