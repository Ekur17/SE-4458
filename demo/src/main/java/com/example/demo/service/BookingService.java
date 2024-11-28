package com.example.demo.service;

import com.example.demo.model.Booking;
import com.example.demo.model.Listing;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ListingRepository listingRepository;

    // Tüm rezervasyonları getir
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // ID ile rezervasyon getir
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Yeni rezervasyon oluştur
    public Booking createBooking(Booking booking, Long listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        if (!listing.isAvailable(booking.getFromDate(), booking.getToDate())) {
            throw new IllegalArgumentException("Listing is not available for the selected dates");
        }

        booking.setListing(listing);
        listing.addBooking(booking);
        return bookingRepository.save(booking);
    }

    // Rezervasyon güncelle
    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        existingBooking.setFromDate(updatedBooking.getFromDate());
        existingBooking.setToDate(updatedBooking.getToDate());
        existingBooking.setGuestNames(updatedBooking.getGuestNames());
        return bookingRepository.save(existingBooking);
    }

    // Rezervasyon sil
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        bookingRepository.delete(booking);
    }
}
