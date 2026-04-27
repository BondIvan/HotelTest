package com.boiv.hotel.hotelapp.model.mapper;

import com.boiv.hotel.hotelapp.exception.CreateHotelException;
import com.boiv.hotel.hotelapp.model.Hotel;
import com.boiv.hotel.hotelapp.model.HotelAddress;
import com.boiv.hotel.hotelapp.model.HotelContact;
import com.boiv.hotel.hotelapp.model.hotelDto.CreateHotelRequestDto;
import com.boiv.hotel.hotelapp.model.hotelDto.HotelShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotelMapper {
    private final AddressMapper addressMapper;
    private final ContactMapper contactMapper;
    private final ArrivalTimeMapper arrivalTimeMapper;

    public Hotel toEntity(CreateHotelRequestDto createHotelRequestDto) {
        Hotel hotel = new Hotel();

        hotel.setName(createHotelRequestDto.name());

        if(createHotelRequestDto.description() != null && !createHotelRequestDto.description().isEmpty())
            hotel.setDescription(createHotelRequestDto.description());

        hotel.setBrand(createHotelRequestDto.brand());
        hotel.setAddress(addressMapper.fromDto(createHotelRequestDto.address()));
        hotel.setContacts(contactMapper.fromDto(createHotelRequestDto.contacts()));
        hotel.setArrivalTime(arrivalTimeMapper.fromDto(createHotelRequestDto.arrivalTime()));

        return hotel;
    }

    public HotelShortResponse toShortDto(Hotel hotel) {
        if(hotel.getAddress() == null)
            throw new CreateHotelException("Cannot create hotel: hotelAddress is null");

        if(hotel.getContacts() == null)
            throw new CreateHotelException("Cannot create hotel: hotelContact is null");

        HotelContact hotelContact = hotel.getContacts();
        HotelAddress hotelAddress = hotel.getAddress();
        String address = String.format("%d %s, %s, %s, %s",
                hotelAddress.getHouseNumber(), hotelAddress.getStreet(),
                hotelAddress.getCity(), hotelAddress.getPostCode(), hotelAddress.getCountry());

        return new HotelShortResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                address,
                hotelContact.getPhone()
        );
    }
}
