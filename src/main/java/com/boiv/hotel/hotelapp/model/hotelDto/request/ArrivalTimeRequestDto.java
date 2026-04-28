package com.boiv.hotel.hotelapp.model.hotelDto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ArrivalTimeRequestDto(
        @NotNull @Future LocalTime checkIn,
        @Future LocalTime checkOut
) { }
