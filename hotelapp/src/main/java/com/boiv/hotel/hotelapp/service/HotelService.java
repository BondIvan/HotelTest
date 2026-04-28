package com.boiv.hotel.hotelapp.service;

import com.boiv.hotel.hotelapp.exception.CreateHotelException;
import com.boiv.hotel.hotelapp.exception.HotelNotFoundException;
import com.boiv.hotel.hotelapp.model.Hotel;
import com.boiv.hotel.hotelapp.model.hotelDto.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.HotelShortResponse;
import com.boiv.hotel.hotelapp.model.hotelDto.SearchHotelFilter;
import com.boiv.hotel.hotelapp.model.mapper.HotelMapper;
import com.boiv.hotel.hotelapp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void addAmenities(Long hotelId, List<String> amenitiesRequest) {
        if(amenitiesRequest == null)
            throw new IllegalArgumentException("Cannot add amenities to the hotel: amenities is null or empty");

        if(amenitiesRequest.isEmpty())
            return;

        Hotel hotelWithAmenities = hotelRepository.findHotelWithAmenitiesById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with id [" + hotelId + "] not found"));

        hotelWithAmenities.getAmenities().addAll(amenitiesRequest);

        hotelRepository.save(hotelWithAmenities);
    }

    public List<HotelShortResponse> searchByFilter(SearchHotelFilter filter) {
        Specification<Hotel> hotelSpecification = HotelSpecification.build(filter);
        List<Hotel> searchedHotels = hotelRepository.findAll(hotelSpecification);

        return searchedHotels.stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }
}
