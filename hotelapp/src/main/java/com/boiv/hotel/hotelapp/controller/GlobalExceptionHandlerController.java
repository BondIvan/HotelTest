package com.boiv.hotel.hotelapp.controller;

import com.boiv.hotel.hotelapp.exception.CreateHotelException;
import com.boiv.hotel.hotelapp.exception.HotelNotFoundException;
import com.boiv.hotel.hotelapp.model.errorDto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(CreateHotelException.class)
    public ResponseEntity<ErrorResponseDto<String>> handleCreateHotelException(CreateHotelException exception) {
        ErrorResponseDto<String> errorResponse = new ErrorResponseDto<>(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponseDto<String>> handleHotelNotFound(HotelNotFoundException exception) {
        ErrorResponseDto<String> errorResponse = new ErrorResponseDto<>(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto<String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponseDto<String> errorResponse = new ErrorResponseDto<>(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ErrorResponseDto<String> errorResponse = new ErrorResponseDto<>("JSON Deserialization Error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto<Map<String, String>>> handleValidationException(MethodArgumentNotValidException exception) {
        ErrorResponseDto<Map<String, String>> errorResponse = new ErrorResponseDto<>(
                exception.getBindingResult().getAllErrors().stream()
                        .collect(Collectors.toMap(
                                error -> ((FieldError) error).getField(),
                                error -> error.getDefaultMessage(),
                                (existing, replacement) -> replacement,
                                HashMap::new
                        ))
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
