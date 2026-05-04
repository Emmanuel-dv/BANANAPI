package com.bananapi.bananapi.controller;

import com.bananapi.bananapi.service.MockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/run")
@RequiredArgsConstructor
public class DispatcherController {

    private final MockService mockService;

    @GetMapping("/{url}")
    public Map<String, Object> getJson(@PathVariable String url) {
        return this.mockService.getJson(url);
    }
}
