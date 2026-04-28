package com.boiv.hotel.hotelapp.model.hotelDto.response;

import java.time.LocalTime;

public record ArrivalTimeResponseDto (
        LocalTime checkIn,
        LocalTime checkOut
) { }
