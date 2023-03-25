package com.musala.dronedelivery.entity;

import com.musala.dronedelivery.enums.DroneModel;
import com.musala.dronedelivery.enums.DroneState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    private BigDecimal weightLimit;

    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState state;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setState(DroneState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "id:" + id + ", serialNumber:" + serialNumber + ", model:" + model + ", batteryCapacity:" + batteryCapacity + ", state:" + state + '}';
    }

}


