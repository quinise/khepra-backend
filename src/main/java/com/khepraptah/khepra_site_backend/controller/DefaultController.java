package com.khepraptah.khepra_site_backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class DefaultController {

    public DefaultController() {
        System.out.println("DefaultController called from controller");
    }

    @GetMapping
    public String getAllAppointments() {
        System.out.println("getAllAppointments called from controller");
        return "Default Controller";
    }
}



