package com.boiv.hotel.hotelapp.model.mapper;

import com.boiv.hotel.hotelapp.model.HotelArrivalTime;
import com.boiv.hotel.hotelapp.model.hotelDto.request.ArrivalTimeRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ArrivalTimeMapper {

    public HotelArrivalTime fromDto(ArrivalTimeRequestDto arrivalTimeRequestDto) {
        return new HotelArrivalTime(
                arrivalTimeRequestDto.checkIn(),
                arrivalTimeRequestDto.checkOut()
        );
    }
}
