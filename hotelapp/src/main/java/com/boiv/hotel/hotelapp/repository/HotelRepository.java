package com.boiv.hotel.hotelapp.repository;

import com.boiv.hotel.hotelapp.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query(value = "SELECT h FROM Hotel h LEFT JOIN FETCH h.amenities WHERE h.id = :hotelId")
    Optional<Hotel> findHotelWithAmenitiesById(@Param("hotelId") Long hotelId);
}
