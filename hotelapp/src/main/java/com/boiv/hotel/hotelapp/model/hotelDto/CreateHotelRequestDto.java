package com.boiv.hotel.hotelapp.model.hotelDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHotelRequestDto (
        @NotBlank String name,
        String description,
        @NotBlank String brand,
        @NotNull AddressDto address,
        @NotNull ContactDto contacts,
        @NotNull ArrivalTimeDto arrivalTime
) { }
