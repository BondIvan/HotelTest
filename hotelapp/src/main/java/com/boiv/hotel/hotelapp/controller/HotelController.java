package com.boiv.hotel.hotelapp.controller;

import com.boiv.hotel.hotelapp.model.hotelDto.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.HotelShortResponse;
import com.boiv.hotel.hotelapp.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

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
}
