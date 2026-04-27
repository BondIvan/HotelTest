package com.boiv.hotel.hotelapp.model.hotelDto;

public record CreateHotelRequestDto (
        String name,
        String description,
        String brand,
        AddressDto address,
        ContactDto contacts,
        ArrivalTimeDto arrivalTime
) { }
