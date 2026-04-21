package com.bananapi.bananapi.dto.requestdto;

import lombok.Data;

import java.util.Map;

@Data
public class RequestMockDTO {

    private String name;

    private Map<String, Object> jsonData;

}
