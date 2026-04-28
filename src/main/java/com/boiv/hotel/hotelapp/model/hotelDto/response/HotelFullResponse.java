package com.boiv.hotel.hotelapp.model.hotelDto.response;

import java.util.Set;

public record HotelFullResponse (
        Long id,
        String name,
        String description,
        String brand,
        AddressResponseDto address,
        ContactResponseDto contacts,
        ArrivalTimeResponseDto arrivalTime,
        Set<String> amenities
) { }
