package com.bananapi.bananapi.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class RequestMockDTO {

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotNull(message = "El JSON no puede ser nulo")
    private Map<String, Object> jsonData;

}
