package com.boiv.hotel.hotelapp.model.hotelDto.response;

public record AddressResponseDto (
        Integer houseNumber,
        String street,
        String city,
        String country,
        String postCode
) { }
