package com.musala.dronedelivery.dto.medicationLoad;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoadCreationDTO {

    @NotEmpty(message = "Load cannot be empty")
    @JsonProperty("medicationCodes")
    private Set<String> itemCodes;

    @Override
    public String toString() {
        return "itemCodes=" + itemCodes;
    }

}
