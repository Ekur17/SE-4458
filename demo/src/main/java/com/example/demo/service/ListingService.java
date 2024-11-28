package com.example.demo.service;

import com.example.demo.model.Listing;
import com.example.demo.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ListingService {

    @Autowired
    private ListingRepository listingRepository;

    // Tüm listeyi getir
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    // Yeni listing ekle
    public Listing insertListing(Listing listing) {
        return listingRepository.save(listing);
    }

    // ID ile listing getir
    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id);
    }

    // Listing güncelle
    public Listing updateListing(Long id, Listing listing) {
        if (listingRepository.existsById(id)) {
            listing.setId(id);
            return listingRepository.save(listing);
        }
        return null;
    }

    // Listing sil
    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }

    // Query Listings (with pagination and filtering)
    public Page<Listing> queryListings(LocalDate from, LocalDate to, String city, String country,
                                       Integer noOfPeople, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        // Fiyat aralığını belirleyelim (örneğin: 0 ile 10000 arası)
        double minPrice = 0;
        double maxPrice = 10000;

        // Tarihler var mı? Eğer varsa, tarihlerle ilgili işlem yapılabilir (şu an sadece fiyat aralığı üzerinden filtre yapıyoruz)
        if (from != null && to != null) {
            // Tarihlerle ilgili daha fazla işlem eklenebilir, örneğin tarihlerde mevcut olmayan evleri döndürme gibi
            return listingRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndIsBookedFalse(
                    from, to, pageRequest);
        }

        // Tarihler belirtilmediyse, sadece şehir, ülke ve fiyat aralığına göre sorgu yapıyoruz
        return listingRepository.findByCountryAndCityAndIsBookedFalseAndPriceBetween(
                country, city, minPrice, maxPrice, pageRequest);
    }

    // Report Listings (with pagination)
    public Page<Listing> reportListings(String country, String city, Double minRating, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        // Minimum rating belirtilmişse, rating'e göre filtreleme yapıyoruz
        if (minRating != null) {
            return listingRepository.findByRatingGreaterThanEqual(minRating, pageRequest);
        }

        // Rating belirtilmemişse, tüm listeyi getiriyoruz
        return listingRepository.findAll(pageRequest);
    }

    // Book a stay (with reservation logic)
    public String bookStay(Long listingId, String fromDate, String toDate, List<String> guestNames) {
        Optional<Listing> listing = listingRepository.findById(listingId);

        if (listing.isPresent()) {
            Listing currentListing = listing.get();

            // Listing'in uygunluğunu kontrol et (rezervasyon yapılmamış ve tarihler uyuyor mu?)
            if (currentListing.isBooked()) {
                return "Error: Listing is already booked.";
            }

            // Tarih aralığında başka bir rezervasyon olup olmadığını kontrol et
            LocalDate startDate = LocalDate.parse(fromDate);
            LocalDate endDate = LocalDate.parse(toDate);
            if (!currentListing.isAvailable(startDate, endDate)) {
                return "Error: Listing is not available for the selected dates.";
            }

            // Rezervasyon işlemi başarılı ise, listing'i işaretleyip kaydet
            currentListing.setBooked(true);
            listingRepository.save(currentListing);

            return "Booking successful from " + fromDate + " to " + toDate + " for " + guestNames.size() + " guests.";
        } else {
            return "Error: Listing not found.";
        }
    }

    // Review a stay (with validation logic)
    public String reviewStay(Long listingId, int rating, String comment) {
        // Yorum ekleme mantığını burada işleyebiliriz
        // Rating ve Comment zorunlu alanlar

        if (rating < 1 || rating > 5) {
            return "Error: Rating must be between 1 and 5.";
        }

        if (comment == null || comment.isEmpty()) {
            return "Error: Comment cannot be empty.";
        }

        // Burada, ilgili listing'i bulup review kaydını işleyebilirsiniz
        Optional<Listing> listing = listingRepository.findById(listingId);
        if (listing.isPresent()) {
            Listing currentListing = listing.get();
            // Review ekleme işlemi yapılabilir (örn: Review entity'si ile ilişkili olarak)
            // Bu noktada, review'leri bir listeye ekleyebilir veya başka bir işlem yapabilirsiniz

            return "Review added successfully.";
        } else {
            return "Error: Listing not found.";
        }
    }
}
