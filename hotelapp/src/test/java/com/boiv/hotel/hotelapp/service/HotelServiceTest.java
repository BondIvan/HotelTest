package com.boiv.hotel.hotelapp.service;

import com.boiv.hotel.hotelapp.exception.CreateHotelException;
import com.boiv.hotel.hotelapp.exception.HotelNotFoundException;
import com.boiv.hotel.hotelapp.model.Hotel;
import com.boiv.hotel.hotelapp.model.hotelDto.SearchHotelFilter;
import com.boiv.hotel.hotelapp.model.hotelDto.request.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.response.HotelFullResponse;
import com.boiv.hotel.hotelapp.model.hotelDto.response.HotelShortResponse;
import com.boiv.hotel.hotelapp.model.mapper.HotelMapper;
import com.boiv.hotel.hotelapp.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelService underTest;

    private final Long hotelId = 1L;

    @Test
    void getHotelWithFullInfo_whenHotelExists_shouldReturnHotelFullResponse() {
        // Given
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);

        HotelFullResponse expectedResponse = new HotelFullResponse(
                hotelId, "name", "description", "brand",
                null, null, null, Set.of("one", "two")
        );

        when(hotelRepository.findHotelWithAmenitiesById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toFullDto(hotel)).thenReturn(expectedResponse);

        // When
        HotelFullResponse actualResponse = underTest.getHotelWithAllInfo(hotelId);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(hotelRepository, times(1)).findHotelWithAmenitiesById(hotelId);
        verify(hotelMapper, times(1)).toFullDto(hotel);
    }

    @Test
    void getHotelWithFullInfo_whenHotelDoesntExists_shouldThrowHotelNotFoundException() {
        // Given
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);

        when(hotelRepository.findHotelWithAmenitiesById(hotelId)).thenReturn(Optional.empty());

        // When
        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> underTest.getHotelWithAllInfo(hotelId)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo("Hotel with id [" + hotelId + "] not found");

        verify(hotelRepository, times(1)).findHotelWithAmenitiesById(hotelId);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void create_whenCreateHotelRequestDtoExists_shouldReturnHotelShortResponse() {
        // Given
        CreateHotelRequestDto createRequest = new CreateHotelRequestDto(
                "name", "description", "brand",
                null, null, null
        );

        Hotel hotel = new Hotel();
        hotel.setName("name");

        Hotel savedHotel = new Hotel();
        savedHotel.setId(hotelId);
        savedHotel.setName("name");

        HotelShortResponse expectedResponse = new HotelShortResponse(
                hotelId, "name", "description", null, null
        );

        when(hotelMapper.toEntity(createRequest)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(hotelMapper.toShortDto(savedHotel)).thenReturn(expectedResponse);

        // When
        HotelShortResponse actualResponse = underTest.create(createRequest);

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(hotelMapper, times(1)).toEntity(createRequest);
        verify(hotelRepository, times(1)).save(hotel);
        verify(hotelMapper, times(1)).toShortDto(savedHotel);
    }

    @Test
    void create_whenCreateHotelRequestDtoIsNull_shouldThrowCreateHotelException() {
        // Given
        CreateHotelRequestDto createRequest = null;

        // When
        CreateHotelException exception = assertThrows(
                CreateHotelException.class,
                () -> underTest.create(createRequest)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo("Cannot create hotel: createHotelRequestDto is null");

        verifyNoInteractions(hotelMapper, hotelRepository);
    }

    @Test
    void addAmenities_whenHotelIdExistsAndAmenitiesListCorrect_shouldReturnNothing() {
        // Given
        List<String> newAmenities = List.of("one", "two");

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.getAmenities().add("one");

        when(hotelRepository.findHotelWithAmenitiesById(hotelId)).thenReturn(Optional.of(hotel));

        // When
        underTest.addAmenities(hotelId, newAmenities);

        // Then
        assertThat(hotel.getAmenities())
                .hasSize(2)
                .contains("one", "two");

        verify(hotelRepository, times(1)).findHotelWithAmenitiesById(hotelId);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenities_whenHotelIdDoesntExists_shouldReturnHotelNotFoundException() {
        // Given
        List<String> newAmenities = List.of("one", "two");
        Long doesntExistsHotelId = 2L;

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.getAmenities().add("one");

        when(hotelRepository.findHotelWithAmenitiesById(anyLong())).thenReturn(Optional.empty());
        // When
        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> underTest.addAmenities(doesntExistsHotelId, newAmenities)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo("Hotel with id [" + doesntExistsHotelId + "] not found");

        verify(hotelRepository).findHotelWithAmenitiesById(doesntExistsHotelId);
        verify(hotelRepository, never()).save(hotel);
    }

    @Test
    void addAmenities_whenHotelIdExistsAndAmenitiesIsNull_shouldReturnIllegalArgumentException() {
        // Given
        List<String> newAmenities = null;

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.getAmenities().add("one");

        // When
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> underTest.addAmenities(hotelId, newAmenities)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo("Cannot add amenities to the hotel: input amenities is null");
        assertThat(hotel.getAmenities())
                .hasSize(1)
                .contains("one");

        verifyNoInteractions(hotelRepository);
    }

    @Test
    void addAmenities_whenHotelIdExistsAndAmenitiesIsEmpty_shouldReturnNothing() {
        // Given
        List<String> newAmenities = new ArrayList<>();

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.getAmenities().add("one");

        // When
        underTest.addAmenities(hotelId, newAmenities);

        // Then
        assertThat(hotel.getAmenities())
                .hasSize(1)
                .contains("one");

        verifyNoInteractions(hotelRepository);
    }

    @Test
    void searchByFilter_whenHotelsFound_shouldReturnListWithHotelShortResponse() {
        // Given
        SearchHotelFilter searchHotelFilter = new SearchHotelFilter(
                "name", "brand", "city", "country", List.of("one")
        );

        Hotel hotel1 = new Hotel();
        hotel1.setId(1L);
        Hotel hotel2 = new Hotel();
        hotel2.setId(2L);

        List<Hotel> foundHotels = List.of(hotel1, hotel2);

        HotelShortResponse expectedResponse1 = new HotelShortResponse(
                1L, "name1", "description1", "address1", "phone1"
        );

        HotelShortResponse expectedResponse2 = new HotelShortResponse(
                2L, "name2", "description2", "address2", "phone2"
        );

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(foundHotels);
        when(hotelMapper.toShortDto(hotel1)).thenReturn(expectedResponse1);
        when(hotelMapper.toShortDto(hotel2)).thenReturn(expectedResponse2);

        // When
        List<HotelShortResponse> actualResponse = underTest.searchByFilter(searchHotelFilter);

        // Then
        assertThat(actualResponse)
                .hasSize(2)
                .contains(expectedResponse1, expectedResponse2);

        verify(hotelRepository, times(1)).findAll(any(Specification.class));
        verify(hotelMapper, times(2)).toShortDto(any(Hotel.class));
    }

    @Test
    void searchByFilter_whenHotelsNotFound_shouldReturnEmptyList() {
        // Given
        SearchHotelFilter searchHotelFilter = new SearchHotelFilter(
                "name", "brand", "city", "country", List.of("one")
        );

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        // When
        List<HotelShortResponse> actualResponse = underTest.searchByFilter(searchHotelFilter);

        // Then
        assertThat(actualResponse).isEmpty();

        verify(hotelRepository).findAll(any(Specification.class));
        verifyNoInteractions(hotelMapper);
    }

    @ParameterizedTest
    @ValueSource(strings = {"brand", "city", "country", "amenities"})
    void histogramByParameter_whenHistParameterIsCorrect_shouldReturnMap(String histParameter) {
        // Given
        List<Object[]> countedByParam = List.of(
                new Object[] {"Parameter 1", 3L},
                new Object[] {"Parameter 2", 5L}
        );

        switch(histParameter) {
            case "brand" -> when(hotelRepository.countByBrand()).thenReturn(countedByParam);
            case "city" -> when(hotelRepository.countByCity()).thenReturn(countedByParam);
            case "country" -> when(hotelRepository.countByCountry()).thenReturn(countedByParam);
            case "amenities" -> when(hotelRepository.countByAmenities()).thenReturn(countedByParam);
        }

        // When
        Map<String, Long> actualMap = underTest.histogramByParameter(histParameter);

        // Then
        assertThat(actualMap)
                .hasSize(2)
                .containsEntry("Parameter 1", 3L)
                .containsEntry("Parameter 2", 5L);

        if (histParameter.equals("brand"))
            verify(hotelRepository).countByBrand();
        if (histParameter.equals("city"))
            verify(hotelRepository).countByCity();
        if (histParameter.equals("country"))
            verify(hotelRepository).countByCountry();
        if (histParameter.equals("amenities"))
            verify(hotelRepository).countByAmenities();
    }

    @Test
    void histogramByParameter_whenInvalidHistParameter_shouldThrowIllegalArgumentException() {
        // Given
        String histParameter = "invalidParameter";

        // When
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> underTest.histogramByParameter(histParameter)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo("Histogram by parameter [" + histParameter + "] is not supported");

        verifyNoInteractions(hotelRepository);
    }

    @Test
    void histogramByParameter_whenHistParameterIsNull_shouldThrowIllegalArgumentException() {
        // Given
        String histParameter = null;

        // When
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> underTest.histogramByParameter(histParameter)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo("Cannot create histogram: histParameter is null");

        verifyNoInteractions(hotelRepository);
    }
}