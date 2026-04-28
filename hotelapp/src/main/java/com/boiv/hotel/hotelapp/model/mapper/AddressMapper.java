package com.boiv.hotel.hotelapp.model.mapper;

import com.boiv.hotel.hotelapp.model.HotelAddress;
import com.boiv.hotel.hotelapp.model.hotelDto.request.AddressRequestDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public HotelAddress fromDto(AddressRequestDto addressRequestDto) {
        return new HotelAddress(
                addressRequestDto.houseNumber(),
                addressRequestDto.street(),
                addressRequestDto.city(),
                addressRequestDto.country(),
                addressRequestDto.postCode()
        );
    }
}
