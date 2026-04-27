package com.boiv.hotel.hotelapp.model.hotelDto;

import java.time.LocalTime;

public record ArrivalTimeDto (
        LocalTime checkIn,
        LocalTime checkOut
) { }
