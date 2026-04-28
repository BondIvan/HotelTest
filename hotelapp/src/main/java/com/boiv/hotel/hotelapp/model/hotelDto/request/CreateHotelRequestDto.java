package com.boiv.hotel.hotelapp.model.hotelDto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHotelRequestDto (
        @NotBlank String name,
        String description,
        @NotBlank String brand,
        @NotNull AddressRequestDto address,
        @NotNull ContactRequestDto contacts,
        @NotNull ArrivalTimeRequestDto arrivalTime
) { }
