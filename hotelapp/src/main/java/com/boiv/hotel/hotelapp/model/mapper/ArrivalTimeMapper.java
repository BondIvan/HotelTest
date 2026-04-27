package com.boiv.hotel.hotelapp.model.mapper;

import com.boiv.hotel.hotelapp.model.HotelArrivalTime;
import com.boiv.hotel.hotelapp.model.hotelDto.ArrivalTimeDto;
import org.springframework.stereotype.Component;

@Component
public class ArrivalTimeMapper {

    public HotelArrivalTime fromDto(ArrivalTimeDto arrivalTimeDto) {
        return new HotelArrivalTime(
                arrivalTimeDto.checkIn(),
                arrivalTimeDto.checkOut()
        );
    }
}
