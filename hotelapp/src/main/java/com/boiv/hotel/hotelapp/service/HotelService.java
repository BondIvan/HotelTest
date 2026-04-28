package com.boiv.hotel.hotelapp.service;

import com.boiv.hotel.hotelapp.exception.CreateHotelException;
import com.boiv.hotel.hotelapp.exception.HotelNotFoundException;
import com.boiv.hotel.hotelapp.model.Hotel;
import com.boiv.hotel.hotelapp.model.hotelDto.request.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.response.HotelFullResponse;
import com.boiv.hotel.hotelapp.model.hotelDto.response.HotelShortResponse;
import com.boiv.hotel.hotelapp.model.hotelDto.SearchHotelFilter;
import com.boiv.hotel.hotelapp.model.mapper.HotelMapper;
import com.boiv.hotel.hotelapp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;

    public HotelFullResponse getHotelWithAllInfo(Long hotelId) {
        Hotel hotel = hotelRepository.findHotelWithAmenitiesById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with id [" + hotelId + "] not found"));

        return hotelMapper.toFullDto(hotel);
    }

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
            throw new IllegalArgumentException("Cannot add amenities to the hotel: input amenities is null");

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

    public Map<String, Long> histogramByParameter(String histParameter) {
        List<Object[]> countByHistParameter = switch (histParameter.toLowerCase()) {
            case "brand" -> hotelRepository.countByBrand();
            case "city" -> hotelRepository.countByCity();
            case "country" -> hotelRepository.countByCountry();
            case "amenities" -> hotelRepository.countByAmenities();
            default -> throw new IllegalArgumentException("Histogram by parameter [" + histParameter + "] is not supported");
        };

        return countByHistParameter.stream()
                .collect(Collectors.toMap(
                        item -> (String) item[0],
                        item -> (Long) item[1]
                ));
    }
}
