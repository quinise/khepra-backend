package com.khepraptah.khepra_site_backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class DefaultController {

    @GetMapping("/")
    public String controllerMethod() {
        return "Default Controller";
    }
}
