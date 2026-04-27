package com.boiv.hotel.hotelapp.model.mapper;

import com.boiv.hotel.hotelapp.model.HotelAddress;
import com.boiv.hotel.hotelapp.model.hotelDto.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public HotelAddress fromDto(AddressDto addressDto) {
        return new HotelAddress(
                addressDto.houseNumber(),
                addressDto.street(),
                addressDto.city(),
                addressDto.country(),
                addressDto.postCode()
        );
    }
}
