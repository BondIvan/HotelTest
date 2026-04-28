package com.boiv.hotel.hotelapp.service;

import com.boiv.hotel.hotelapp.model.Hotel;
import com.boiv.hotel.hotelapp.model.hotelDto.SearchHotelFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class HotelSpecification {
    public static Specification<Hotel> build(SearchHotelFilter filter) {
        return hasName(filter.name())
                .and(hasBrand(filter.brand()))
                .and(hasCity(filter.city()))
                .and(hasCountry(filter.country()))
                .and(hasAmenities(filter.amenities()));
    }

    public static Specification<Hotel> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isEmpty())
                return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")), name.toLowerCase());
        };
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, criteriaBuilder) -> {
            if(brand == null || brand.isEmpty())
                return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase());
        };
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if(city == null || city.isEmpty())
                return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("address").get("city")), city.toLowerCase());
        };
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            if(country == null || country.isEmpty())
                return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("address").get("country")), country.toLowerCase());
        };
    }

    // select h.id from hotel h join hotel_amenities ha on h.id = ha.hotel_id
    // where ha.amenity IN('Free WiFi', 'Meeting rooms') group by h.id having count(*) = 2
    public static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            if(amenities == null || amenities.isEmpty())
                return criteriaBuilder.conjunction();

            List<String> lowerCaseAmenities = amenities.stream()
                    .map(String::toLowerCase)
                    .toList();

            Join<Hotel, String> ha = root.join("amenities", JoinType.INNER);
            Predicate where = criteriaBuilder.lower(ha).in(lowerCaseAmenities);
            Predicate having = criteriaBuilder.equal(criteriaBuilder.count(ha), amenities.size());

            query.groupBy(root.get("id"));
            query.having(having);

            return where;
        };
    }
}
