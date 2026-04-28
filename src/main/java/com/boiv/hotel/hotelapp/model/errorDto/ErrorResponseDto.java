package com.boiv.hotel.hotelapp.model.errorDto;

public record ErrorResponseDto<T> (
        T data
) { }
