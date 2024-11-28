package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID otomatik oluşturulacak
    private Long id;

    private String country; // Ülke
    private String city;    // Şehir
    private Integer noOfPeople; // Kaç kişinin kalabileceği
    private Double price;   // Günlük fiyat
    private Double rating = 0.0; // Ortalama değerlendirme puanı, başlangıçta 0.0
    private boolean isBooked = false; // Rezervasyon durumu

    private LocalDate fromDate; // Rezervasyonun başlangıç tarihi
    private LocalDate toDate;   // Rezervasyonun bitiş tarihi

    // Booking ile ilişki (One-to-Many)
    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    // Getter ve Setter metotları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(Integer noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // Booking eklemek için yardımcı metot
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setListing(this);
    }

    // Booking kaldırmak için yardımcı metot
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setListing(null);
    }

    // Belirtilen tarihlerde müsaitlik kontrolü
    public boolean isAvailable(LocalDate fromDate, LocalDate toDate) {
        for (Booking booking : bookings) {
            if (!(toDate.isBefore(booking.getFromDate()) || fromDate.isAfter(booking.getToDate()))) {
                return false; // Bu tarihlerde rezervasyon var
            }
        }
        return true; // Bu tarihlerde müsait
    }

    // Rezervasyon durumunu güncelleme
    public void markAsBooked() {
        this.isBooked = true;
    }

    public void markAsAvailable() {
        this.isBooked = false;
    }

    // Ortalama değerlendirme puanı hesaplama
    public void updateRating() {
        double totalRating = 0.0;
        int count = 0;

        for (Booking booking : bookings) {
            for (Review review : booking.getReviews()) {
                totalRating += review.getRating();
                count++;
            }
        }

        this.rating = count > 0 ? totalRating / count : 0.0;
    }
}
