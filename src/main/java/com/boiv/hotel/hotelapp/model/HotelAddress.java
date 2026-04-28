package com.boiv.hotel.hotelapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class HotelAddress {
    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(name = "post_code", nullable = false)
    private String postCode;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HotelAddress that = (HotelAddress) o;
        return Objects.equals(houseNumber, that.houseNumber)
                && Objects.equals(street, that.street)
                && Objects.equals(city, that.city)
                && Objects.equals(country, that.country)
                && Objects.equals(postCode, that.postCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseNumber, street, city, country, postCode);
    }
}
