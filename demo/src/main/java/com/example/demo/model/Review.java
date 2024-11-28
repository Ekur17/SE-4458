package com.example.demo.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID otomatik artan olacak
    private Long id;

    @ManyToOne // Review bir booking ile ilişkilidir
    @JoinColumn(name = "booking_id", nullable = false) // Foreign key: booking tablosuna referans
    private Booking booking;

    private double rating; // Rating (1-5 arası)

    @Column(length = 500) // Yorum uzunluğu sınırlanabilir
    private String comment;

    private String status; // Successful, Error

    // Parametresiz constructor
    public Review() {}

    // Constructor
    public Review(Long id, Booking booking, double rating, String comment, String status) {
        this.id = id;
        this.booking = booking;
        this.rating = rating;
        this.comment = comment;
        this.status = status;
    }

    // Getter ve Setter metodları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
