package com.boiv.hotel.hotelapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class HotelArrivalTime {
    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @Column(name = "check_out", nullable = true)
    private LocalTime checkOut;
}
