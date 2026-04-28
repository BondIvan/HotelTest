package com.boiv.hotel.hotelapp.controller;

import com.boiv.hotel.hotelapp.exception.HotelNotFoundException;
import com.boiv.hotel.hotelapp.model.hotelDto.SearchHotelFilter;
import com.boiv.hotel.hotelapp.model.hotelDto.request.AddressRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.request.ArrivalTimeRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.request.ContactRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.request.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.response.*;
import com.boiv.hotel.hotelapp.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void getAllHotelsShortInfo_whenHotelsExists_shouldReturnListWithHotelShortResponseAnd200() throws Exception {
        // Given
        HotelShortResponse hotelShortResponse1 = new HotelShortResponse(
                1L, "name1", "description1", "address1", "phone1"
        );

        HotelShortResponse hotelShortResponse2 = new HotelShortResponse(
                2L, "name2", "description2", "address2", "phone2"
        );

        List<HotelShortResponse> responseList = List.of(hotelShortResponse1, hotelShortResponse2);
        SearchHotelFilter filter = new SearchHotelFilter(null, null, null, null, null);

        when(hotelService.searchByFilter(filter)).thenReturn(responseList);

        // When & Then
        mockMvc.perform(get("/hotels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("name2"));

        verify(hotelService).searchByFilter(filter);
    }

    @Test
    void getAllHotelsShortInfo_whenResultIsEmpty_shouldReturnEmptyListAnd200() throws Exception {
        // Given
        SearchHotelFilter filter = new SearchHotelFilter(null, null, null, null, null);
        when(hotelService.searchByFilter(filter)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getHotelFullInfo_whenHotelIdExists_shouldReturnHotelFullResponseAnd200() throws Exception {
        // Given
        Long hotelId = 1L;
        AddressResponseDto addressResponse = new AddressResponseDto(
                9, "street", "city", "country", "12345"
        );

        ContactResponseDto contactsResponse = new ContactResponseDto("123456789", "hotel@mail.com");

        ArrivalTimeResponseDto arrivalTimeResponse = new ArrivalTimeResponseDto(
                LocalTime.of(14, 0), LocalTime.of(12, 0)
        );

        HotelFullResponse hotelFullResponse = new HotelFullResponse(
                hotelId, "name", "description", "brand",
                addressResponse, contactsResponse, arrivalTimeResponse, Set.of("one", "two")
        );

        when(hotelService.getHotelWithAllInfo(hotelId)).thenReturn(hotelFullResponse);

        // When & Then
        mockMvc.perform(get("/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.address.city").value("city"))
                .andExpect(jsonPath("$.contacts.email").value("hotel@mail.com"))
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities", hasItems("one", "two")));

        verify(hotelService).getHotelWithAllInfo(hotelId);
    }

    @Test
    void getHotelFullInfo_whenHotelIdDoesntExists_shouldReturn404() throws Exception {
        // Given
        Long hotelId = 1L;
        String errorMessage = "Hotel with id [" + hotelId + "] not found";

        when(hotelService.getHotelWithAllInfo(hotelId)).thenThrow(new HotelNotFoundException(errorMessage));

        // When & Then
        mockMvc.perform(get("/hotels/{id}", hotelId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").value(errorMessage));
    }

    @Test
    void createNewHotel_whenCreateHotelRequestDtoIsCorrect_shouldReturnHotelShortResponseAnd201() throws Exception {
        // Given
        Long hotelId = 1L;
        AddressRequestDto addressRequestDto = new AddressRequestDto(
                9, "street", "city", "country", "123456"
        );

        ContactRequestDto contactRequestDto = new ContactRequestDto("123456789", "hotel@mail.com");

        ArrivalTimeRequestDto arrivalTimeRequestDto = new ArrivalTimeRequestDto(
                LocalTime.now().plusMinutes(1), LocalTime.now().plusHours(24)
        );

        CreateHotelRequestDto createHotelRequestDto = new CreateHotelRequestDto(
                "name", "description", "brand",
                addressRequestDto, contactRequestDto, arrivalTimeRequestDto
        );

        HotelShortResponse hotelShortResponse = new HotelShortResponse(
                hotelId, "name", "description", "address", "123456789"
        );

        when(hotelService.create(createHotelRequestDto)).thenReturn(hotelShortResponse);

        // When & Then
        mockMvc.perform(post("/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createHotelRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.address").value("address"));

        verify(hotelService).create(createHotelRequestDto);
    }

    @Test
    void createNewHotel_whenValidationErrorWithCreateHotelRequestDto_shouldReturn400() throws Exception {
        // Given
        CreateHotelRequestDto invalidCreateHotelRequestDto = new CreateHotelRequestDto(
                null, "description", "brand",
                null, null, null
        );

        // When & Then
        mockMvc.perform(post("/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCreateHotelRequestDto)))
                .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.data.name").exists());

        verifyNoInteractions(hotelService);
    }

    @Test
    void addAmenitiesToHotel_whenHotelIdExistsAndAmenitiesRequestCorrect_shouldReturn200() throws Exception {
        // Given
        Long hotelId = 1L;
        List<String> newAmenities = List.of("one", "two");

        doNothing().when(hotelService).addAmenities(eq(hotelId), anyList());

        // When & Then
        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAmenities)))
                .andExpect(status().isOk());

        verify(hotelService).addAmenities(eq(hotelId), eq(newAmenities));
    }

    @Test
    void addAmenitiesToHotel_whenHotelIdDoesntExists_shouldReturn404() throws Exception {
        // Given
        Long invalidHotelId = 2L;
        List<String> newAmenities = List.of("one", "two");
        String errorMessage = "Hotel with id [" + invalidHotelId + "] not found";

        doThrow(new HotelNotFoundException(errorMessage)).when(hotelService).addAmenities(eq(invalidHotelId), anyList());

        // When & Then
        mockMvc.perform(post("/hotels/{id}/amenities", invalidHotelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAmenities)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").value(errorMessage));

        verify(hotelService).addAmenities(eq(invalidHotelId), eq(newAmenities));
    }

    @Test
    void addAmenitiesToHotel_whenAmenitiesRequestIsNull_shouldReturn400() throws Exception {
        // Given
        Long hotelId = 1L;
        List<String> newAmenities = null;
        String errorMessage = "JSON Deserialization Error";

        doThrow(new HttpMessageNotReadableException(errorMessage)).when(hotelService).addAmenities(eq(hotelId), eq(newAmenities));

        // When & Then
        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("null")) // null objectMapper.writeValueAsString(newAmenities)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value(errorMessage));

        verifyNoInteractions(hotelService);
    }

    @Test
    void search_whenAllParametersAllowedAndCorrect_shouldReturnListWithHotelShortResponseAnd200() throws Exception {
        // Given
        SearchHotelFilter filter = new SearchHotelFilter(
                "name", "brand", "city", "country", List.of("one", "two")
        );

        HotelShortResponse hotel1 = new HotelShortResponse(
                1L, "name1", "description1", "address1", "1123456789"
        );
        HotelShortResponse hotel2 = new HotelShortResponse(
                2L, "name2", "description2", "address2", "2123456789"
        );

        List<HotelShortResponse> response = List.of(hotel1, hotel2);

        when(hotelService.searchByFilter(filter)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/search")
                .param("name", "name")
                .param("brand", "brand")
                .param("city", "city")
                .param("country", "country")
                .param("amenities", "one", "two"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[0].address").value("address1"))
                .andExpect(jsonPath("$[1].address").value("address2"))
                .andExpect(jsonPath("$[0].phone").value("1123456789"))
                .andExpect(jsonPath("$[1].phone").value("2123456789"));

        verify(hotelService).searchByFilter(filter);
    }

    @Test
    void search_whenOneOfParametersInvalid_shouldThrowIllegalArgumentExceptionAnd400() throws Exception {
        // Given
        String invalidParameter = "invalidParameter";
        String errorMessage = "Search by parameters " + List.of(invalidParameter) + " are not supported";

        // When & Then
        mockMvc.perform(get("/search")
                        .param("name", "name")
                        .param(invalidParameter, "invalidParameter"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value(containsString(errorMessage)));

        verifyNoInteractions(hotelService);
    }

    @Test
    void histogram_whenHistParameterIsCorrect_shouldReturnMapAnd200() throws Exception {
        // Given
        String histParameter = "city";
        Map<String, Long> expectedData = Map.of(
                "city1", 3L,
                "city2", 5L
        );

        when(hotelService.histogramByParameter(histParameter)).thenReturn(expectedData);

        // When & Then
        mockMvc.perform(get("/histogram/{param}", histParameter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city1").value(3))
                .andExpect(jsonPath("$.city2").value(5));

        verify(hotelService).histogramByParameter(histParameter);
    }

    @Test
    void histogram_whenInvalidHistParameter_shouldReturn400() throws Exception {
        // Given
        String invalidHistParameter = "invalidHistParameter";
        String errorMessage = "Histogram by parameter [" + invalidHistParameter + "] is not supported";

        when(hotelService.histogramByParameter(invalidHistParameter)).thenThrow(new IllegalArgumentException(errorMessage));

        // When & Then
        mockMvc.perform(get("/histogram/{param}", invalidHistParameter))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value(errorMessage));

        verify(hotelService).histogramByParameter(invalidHistParameter);
    }

}