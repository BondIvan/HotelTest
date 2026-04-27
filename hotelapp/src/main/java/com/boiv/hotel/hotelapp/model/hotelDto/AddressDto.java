package com.boiv.hotel.hotelapp.model.hotelDto;

public record AddressDto (
        Integer houseNumber,
        String street,
        String city,
        String country,
        String postCode
) { }
