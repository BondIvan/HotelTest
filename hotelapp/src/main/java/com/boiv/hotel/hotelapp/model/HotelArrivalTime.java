package com.boiv.hotel.hotelapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HotelArrivalTime that = (HotelArrivalTime) o;
        return Objects.equals(checkIn, that.checkIn)
                && Objects.equals(checkOut, that.checkOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkIn, checkOut);
    }
}
