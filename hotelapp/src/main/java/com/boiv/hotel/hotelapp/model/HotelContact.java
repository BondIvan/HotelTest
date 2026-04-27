package com.boiv.hotel.hotelapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
