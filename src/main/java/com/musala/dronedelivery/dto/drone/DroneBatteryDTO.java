package com.musala.dronedelivery.dto.drone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DroneBatteryDTO {

    @JsonProperty("serialNumber")
    private String serialNumber;

    @JsonProperty("batteryCapacity")
    private Integer batteryCapacity;

}
