package com.example.demo.repository;

import com.example.demo.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    // Ülke ve şehirdeki açık (rezervasyonu yapılmamış) listelemeleri sorgulayan method
    Page<Listing> findByCountryAndCityAndIsBookedFalseAndPriceBetween(
            String country, String city, double minPrice, double maxPrice, Pageable pageable);

    // Dereceye göre listeleme, sayfalama ile
    Page<Listing> findByRatingGreaterThanEqual(Double minRating, Pageable pageable);

    // Farklı parametrelerle listeleme sorgusu
    Page<Listing> findByCountryAndCityAndIsBookedFalse(
            String country, String city, Pageable pageable);

    // Tüm listeyi almak için, sayfalama ile
    Page<Listing> findAll(Pageable pageable);

    // Belirli bir tarihlerde açık olan listelemeleri sorgulayan method
    Page<Listing> findByFromDateLessThanEqualAndToDateGreaterThanEqualAndIsBookedFalse(
            LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
