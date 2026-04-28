package com.boiv.hotel.hotelapp.model.hotelDto;

import com.boiv.hotel.hotelapp.model.hotelDto.response.AddressResponseDto;
import com.boiv.hotel.hotelapp.model.hotelDto.response.ArrivalTimeResponseDto;
import com.boiv.hotel.hotelapp.model.hotelDto.response.ContactResponseDto;

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
