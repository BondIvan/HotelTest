package com.boiv.hotel.hotelapp.model.hotelDto;

public record HotelShortResponse (
        Long id,
        String name,
        String description,
        String address,
        String phone
) { }
