package com.musala.dronedelivery.repository;

import com.musala.dronedelivery.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    boolean existsByName(String name);

    boolean existsByCode(String code);

    @Query("SELECT m.code FROM Medication m WHERE m.code IN :codes")
    List<String> findExistingCodes(@Param("codes") Set<String> codes);

    List<Medication> findAllByCodeIn(Set<String> medicationCodes);
}
