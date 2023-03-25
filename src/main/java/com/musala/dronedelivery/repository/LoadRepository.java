package com.musala.dronedelivery.repository;

import com.musala.dronedelivery.generic.Load;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoadRepository extends JpaRepository<Load, Long> {

    Optional<Load> findByDroneSerialNumber(String droneSerialNumber);
}
