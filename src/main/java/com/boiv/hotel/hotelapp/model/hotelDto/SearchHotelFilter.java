package com.boiv.hotel.hotelapp.model.hotelDto;

import java.util.List;

public record SearchHotelFilter (
        String name,
        String brand,
        String city,
        String country,
        List<String> amenities
) { }
