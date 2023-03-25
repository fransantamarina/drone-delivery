package com.musala.dronedelivery.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorDTO {

    private String status;
    private String error;
    private List<FieldError> errors;

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class FieldError {
        private String field;
        private String message;
    }

}

