package com.boiv.hotel.hotelapp.model.hotelDto.request;

import jakarta.validation.constraints.NotBlank;

public record ContactRequestDto(
        @NotBlank String phone,
        @NotBlank String email
) { }
