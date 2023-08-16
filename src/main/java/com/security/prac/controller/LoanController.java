package com.security.prac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myLoans")
public class LoanController {
    @GetMapping
    public String index() {
        return "index baby";
    }
}
