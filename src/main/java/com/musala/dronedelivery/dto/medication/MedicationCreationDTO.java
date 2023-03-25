package com.musala.dronedelivery.dto.medication;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MedicationCreationDTO {

    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Name can only only letters, numbers, hyphens and underscores")
    private String name;

    @NotEmpty(message = "Code cannot be empty")
    @Pattern(regexp = "^[A-Z_0-9]*$", message = "Code can only contain uppercase letters, underscore, and numbers")
    private String code;

    @NotNull
    @Positive(message = "Must be greater than or equal to 0")
    private BigDecimal weight;

    @URL(message = "Image URL is not valid")
    private String image;

}
