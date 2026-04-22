package com.bananapi.bananapi.dto.responsedto;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseMockDTO {

    private String name;

    private String url;

    private Map<String, Object> jsonData;

}
