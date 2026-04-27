package com.boiv.hotel.hotelapp.model.hotelDto;

import jakarta.validation.constraints.NotBlank;

public record ContactDto(
        @NotBlank String phone,
        @NotBlank String email
) { }
