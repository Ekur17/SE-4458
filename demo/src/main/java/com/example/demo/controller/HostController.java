package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HostController {

    // Host kullanıcılarına özel bir dashboard endpoint'i
    @GetMapping("/api/host/dashboard")
    public String getHostDashboard() {
        return "Host Dashboard - Erişim başarılı!";
    }
}
