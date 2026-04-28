package com.boiv.hotel.hotelapp.controller;

import com.boiv.hotel.hotelapp.model.hotelDto.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.HotelFullResponse;
import com.boiv.hotel.hotelapp.model.hotelDto.HotelShortResponse;
import com.boiv.hotel.hotelapp.model.hotelDto.SearchHotelFilter;
import com.boiv.hotel.hotelapp.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class HotelController {
    private static final Set<String> ALLOWED_SEARCH_PARAMETERS = Set.of("name", "brand", "city", "country", "amenities");

    private final HotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortResponse>> getAllHotelShortInfo() {
        SearchHotelFilter filter = new SearchHotelFilter(null, null, null, null, null);
        List<HotelShortResponse> response = hotelService.searchByFilter(filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelFullResponse> getHotelFullInfo(@PathVariable("id") Long hotelId) {
        HotelFullResponse response = hotelService.getHotelWithAllInfo(hotelId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/hotels")
    public ResponseEntity<HotelShortResponse> createNewHotel(@RequestBody @Valid CreateHotelRequestDto createHotelRequestDto) {
        HotelShortResponse response = hotelService.create(createHotelRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenitiesToHotel(
            @PathVariable("id") Long hotelId,
            @RequestBody List<String> amenitiesRequest) {
        hotelService.addAmenities(hotelId, amenitiesRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelShortResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities,
            @RequestParam Map<String, String> allParams
    ) {
        Set<String> params = allParams.keySet();
        params.removeIf(ALLOWED_SEARCH_PARAMETERS::contains);
        if(!params.isEmpty())
            throw new IllegalArgumentException("Search by parameters " + params + " are not supported");

        SearchHotelFilter filter = new SearchHotelFilter(name, brand, city, country, amenities);
        List<HotelShortResponse> response = hotelService.searchByFilter(filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> histogram(@PathVariable("param") String histParameter) {
        Map<String, Long> response = hotelService.histogramByParameter(histParameter);
        return ResponseEntity.ok(response);
    }
}
