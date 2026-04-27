package com.boiv.hotel.hotelapp.model.hotelDto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ArrivalTimeDto (
        @NotNull @Future LocalTime checkIn,
        @Future LocalTime checkOut
) { }
