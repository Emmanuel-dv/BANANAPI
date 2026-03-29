package com.bananapi.bananapi.dto.requestdto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class MockDTO {

    private String name;

    private String url;

    private LocalDateTime creationDate;

    private LocalDateTime lastUsage;

    private UserRegistrationDTO user;

    private Map<String, Object> jsonData;

}
