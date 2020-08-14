package com.crib.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/")
    public String testRoute() {
        return "Crib is up and running!";
    }

    @PostMapping("/")
    public String testRoute2() {
        return "Crib is up and running!";
    }
}
