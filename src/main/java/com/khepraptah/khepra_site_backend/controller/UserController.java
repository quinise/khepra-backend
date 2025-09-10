package com.khepraptah.khepra_site_backend.controller;

import com.khepraptah.khepra_site_backend.dto.ExistsResponse;
import com.khepraptah.khepra_site_backend.service.FirebaseUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final FirebaseUserService userService;

    public UserController(FirebaseUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/exists")
    public ResponseEntity<ExistsResponse> existsByEmail(
            @RequestParam @NotBlank @Email String email) {
        boolean exists = userService.existsByEmail(email);
        if (!exists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "USER_NOT_FOUND"
            );
        }
        return ResponseEntity.ok(new ExistsResponse(true));
    }
}
