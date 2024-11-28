package com.example.demo.controller;

import com.example.demo.model.Listing;
import com.example.demo.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

    // Tüm listingleri getir
    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

    // Yeni listing ekle (Insert Listing)
    @PostMapping
    public ResponseEntity<String> insertListing(@RequestBody Listing listing) {
        Listing savedListing = listingService.insertListing(listing);
        return savedListing != null
                ? ResponseEntity.ok("Listing successfully inserted!")
                : ResponseEntity.badRequest().body("Error inserting listing.");
    }

    // Belirli bir listingi getir
    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable Long id) {
        return listingService.getListingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listing güncelle
    @PutMapping("/{id}")
    public ResponseEntity<String> updateListing(@PathVariable Long id, @RequestBody Listing listing) {
        Listing updatedListing = listingService.updateListing(id, listing);
        return updatedListing != null
                ? ResponseEntity.ok("Listing successfully updated!")
                : ResponseEntity.notFound().build();
    }

    // Listing sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }

    // Query Listings (Mobile App - Guest) - Paging ve filtering eklenmiş
    @GetMapping("/query")
    public Page<Listing> queryListings(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer noOfPeople,
            @RequestParam(defaultValue = "0") int page,  // Default olarak sayfa 0
            @RequestParam(defaultValue = "10") int size   // Default olarak 10 liste
    ) {
        LocalDate from = fromDate != null ? LocalDate.parse(fromDate) : null;
        LocalDate to = toDate != null ? LocalDate.parse(toDate) : null;
        return listingService.queryListings(from, to, city, country, noOfPeople, page, size);
    }

    // Book a Stay (Mobile App - Guest)
    @PostMapping("/book")
    public ResponseEntity<String> bookStay(@RequestParam Long listingId,
                                           @RequestParam String fromDate,
                                           @RequestParam String toDate,
                                           @RequestBody List<String> guestNames) {
        String response = listingService.bookStay(listingId, fromDate, toDate, guestNames);
        return response.contains("Error")
                ? ResponseEntity.badRequest().body(response)
                : ResponseEntity.ok(response);
    }

    // Review a Stay (Mobile App - Guest)
    @PostMapping("/review")
    public ResponseEntity<String> reviewStay(@RequestParam Long stayId,
                                             @RequestParam int rating,
                                             @RequestParam String comment) {
        String response = listingService.reviewStay(stayId, rating, comment);
        return response.startsWith("Error")
                ? ResponseEntity.badRequest().body(response)
                : ResponseEntity.ok(response);
    }

    // Report Listings (Web Site Admin) - Paging eklenmiş
    @GetMapping("/report")
    public Page<Listing> reportListings(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,  // Default olarak sayfa 0
            @RequestParam(defaultValue = "10") int size   // Default olarak 10 liste
    ) {
        return listingService.reportListings(country, city, minRating, page, size);
    }
}
