package com.security.prac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myBalence")
public class BalenceController {
    @GetMapping
    public String index() {
        return "index baby";
    }
}