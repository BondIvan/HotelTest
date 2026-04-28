package com.boiv.hotel.hotelapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class HotelContact {
    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HotelContact that = (HotelContact) o;
        return Objects.equals(phone, that.phone)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, email);
    }
}
