package com.boiv.hotel.hotelapp.controller;

import com.boiv.hotel.hotelapp.model.hotelDto.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.HotelShortResponse;
import com.boiv.hotel.hotelapp.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping("/hotels")
    public ResponseEntity<HotelShortResponse> createNewHotel(@RequestBody @Valid CreateHotelRequestDto createHotelRequestDto) {
        HotelShortResponse response = hotelService.create(createHotelRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
