package com.example.demo.repository;

import com.example.demo.model.Booking;
import com.example.demo.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Belirli bir listing için verilen tarih aralığında rezervasyonları bul
    List<Booking> findByListingAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            Listing listing, LocalDate fromDate, LocalDate toDate);

    // Belirli bir listing ile ilişkili tüm rezervasyonları al
    List<Booking> findByListing(Listing listing);

    // Belirli bir tarihler arasında rezervasyon yapılıp yapılmadığını kontrol et
    List<Booking> findByFromDateLessThanEqualAndToDateGreaterThanEqual(
            LocalDate fromDate, LocalDate toDate);
}
