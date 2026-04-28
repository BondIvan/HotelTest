package com.boiv.hotel.hotelapp.model.hotelDto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record ArrivalTimeResponseDto (
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkIn,

        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOut
) { }
