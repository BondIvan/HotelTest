package com.boiv.hotel.hotelapp.model.mapper;

import com.boiv.hotel.hotelapp.model.HotelContact;
import com.boiv.hotel.hotelapp.model.hotelDto.request.ContactRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public HotelContact fromDto(ContactRequestDto contactRequestDto) {
        return new HotelContact(
                contactRequestDto.phone(),
                contactRequestDto.email()
        );
    }
}
