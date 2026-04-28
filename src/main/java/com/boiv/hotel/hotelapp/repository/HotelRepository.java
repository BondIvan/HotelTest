package com.boiv.hotel.hotelapp.repository;

import com.boiv.hotel.hotelapp.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query(value = "SELECT h FROM Hotel h LEFT JOIN FETCH h.amenities WHERE h.id = :hotelId")
    Optional<Hotel> findHotelWithAmenitiesById(@Param("hotelId") Long hotelId);

    @Query(value = "SELECT h.brand, COUNT(h) FROM Hotel h GROUP BY h.brand")
    List<Object[]> countByBrand();

    @Query(value = "SELECT h.address.city, COUNT(h) FROM Hotel h GROUP BY h.address.city")
    List<Object[]> countByCity();

    @Query(value = "SELECT h.address.country, COUNT(h) FROM Hotel h GROUP BY h.address.country")
    List<Object[]> countByCountry();

    @Query(value = "SELECT ha, COUNT(h) FROM Hotel h JOIN h.amenities ha GROUP BY ha")
    List<Object[]> countByAmenities();
}
