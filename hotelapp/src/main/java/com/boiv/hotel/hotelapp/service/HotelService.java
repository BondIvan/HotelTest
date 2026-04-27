package com.boiv.hotel.hotelapp.service;

import com.boiv.hotel.hotelapp.exception.CreateHotelException;
import com.boiv.hotel.hotelapp.model.Hotel;
import com.boiv.hotel.hotelapp.model.hotelDto.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.HotelShortResponse;
import com.boiv.hotel.hotelapp.model.mapper.HotelMapper;
import com.boiv.hotel.hotelapp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;

    @Transactional
    public HotelShortResponse create(CreateHotelRequestDto createHotelRequestDto) {
        if(createHotelRequestDto == null)
            throw new CreateHotelException("Cannot create hotel: createHotelRequestDto is null");

        Hotel hotel = hotelMapper.toEntity(createHotelRequestDto);
        Hotel createdHotel = hotelRepository.save(hotel);

        return hotelMapper.toShortDto(createdHotel);
    }
}
