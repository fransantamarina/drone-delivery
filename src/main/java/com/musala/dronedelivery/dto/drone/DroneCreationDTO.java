package com.musala.dronedelivery.dto.drone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.musala.dronedelivery.enums.DroneModel;
import com.musala.dronedelivery.annotation.ValidEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DroneCreationDTO {

    @NotNull(message = "Serial number cannot be null")
    @Length(min = 5, max = 100, message = "Serial number must be between 5 and 100 characters")
    @JsonProperty("serialNumber")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @ValidEnum(enumClass = DroneModel.class, message = "Invalid drone model")
    private String model;

    @Nullable
    @JsonProperty("weightLimit")
    private BigDecimal weightLimit;

}
