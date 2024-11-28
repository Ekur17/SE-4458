package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID otomatik artan olacak
    private Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false) // Foreign key: listing tablosuna referans
    private Listing listing;

    private LocalDate fromDate; // Başlangıç tarihi
    private LocalDate toDate;   // Bitiş tarihi

    @ElementCollection // Basit koleksiyonlar için kullanılır
    @CollectionTable(name = "booking_guest_names", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "guest_name") // İsimlerin saklanacağı sütun adı
    private List<String> guestNames = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Getter ve Setter metodları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public List<String> getGuestNames() {
        return guestNames;
    }

    public void setGuestNames(List<String> guestNames) {
        this.guestNames = guestNames;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // Booking'e review eklemek için yardımcı metot
    public void addReview(Review review) {
        reviews.add(review);
        review.setBooking(this); // Review içinde booking ilişkisini ayarla
    }

    // Booking'ten review kaldırmak için yardımcı metot
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setBooking(null); // Review içinde booking ilişkisini kaldır
    }
}
