package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuestController {

    // Guest kullanıcılarına açık bir endpoint
    @GetMapping("/api/guest/dashboard")
    public String getGuestDashboard() {
        return "Guest Dashboard - Erişim başarılı!";
    }
}
