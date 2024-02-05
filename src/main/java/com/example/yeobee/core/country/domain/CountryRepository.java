package com.example.yeobee.core.country.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends JpaRepository<Country, String> {

    @Query("SELECT c FROM Country c LEFT JOIN TripCountry tc ON c = tc.country GROUP BY c ORDER BY COUNT(tc) DESC, c.name ASC")
    List<Country> findAllCountriesWithMostTrips();
}
