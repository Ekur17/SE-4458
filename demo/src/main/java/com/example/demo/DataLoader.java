package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DataLoader(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Admin Kullanıcı
        if (!userRepository.existsByUsername("admin")) {  // Kullanıcı var mı kontrolü
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ADMIN));
            userRepository.save(admin); // Veritabanına kaydet
            System.out.println("Admin kullanıcısı oluşturuldu: " + admin.getUsername());
        }

        // Host Kullanıcı
        if (!userRepository.existsByUsername("host")) {  // Kullanıcı var mı kontrolü
            User host = new User();
            host.setUsername("host");
            host.setPassword(passwordEncoder.encode("host123"));
            host.setRoles(Set.of(Role.HOST));
            userRepository.save(host); // Veritabanına kaydet
            System.out.println("Host kullanıcısı oluşturuldu: " + host.getUsername());
        }

        // Guest Kullanıcı
        if (!userRepository.existsByUsername("ahmet")) {  // Kullanıcı var mı kontrolü
            User guest = new User();
            guest.setUsername("ahmet");
            guest.setPassword(passwordEncoder.encode("guest123"));
            guest.setRoles(Set.of(Role.GUEST));
            userRepository.save(guest); // Veritabanına kaydet
            System.out.println("Guest kullanıcısı oluşturuldu: " + guest.getUsername());
        }

        // Ali (Guest) Kullanıcı
        if (!userRepository.existsByUsername("ali")) {  // Kullanıcı var mı kontrolü
            User ali = new User();
            ali.setUsername("ali");
            ali.setPassword(passwordEncoder.encode("ahmet123"));
            ali.setRoles(Set.of(Role.GUEST));
            userRepository.save(ali); // Veritabanına kaydet
            System.out.println("Guest kullanıcısı oluşturuldu: " + ali.getUsername());
        }
    }
}
