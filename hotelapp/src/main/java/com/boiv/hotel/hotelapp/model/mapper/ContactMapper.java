package com.boiv.hotel.hotelapp.model.mapper;

import com.boiv.hotel.hotelapp.model.HotelContact;
import com.boiv.hotel.hotelapp.model.hotelDto.ContactDto;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public HotelContact fromDto(ContactDto contactDto) {
        return new HotelContact(
                contactDto.phone(),
                contactDto.email()
        );
    }
}
