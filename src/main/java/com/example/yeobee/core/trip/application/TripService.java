package com.example.yeobee.core.trip.application;

import com.example.yeobee.common.dto.request.PageRequestDto;
import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.country.domain.CountryRepository;
import com.example.yeobee.core.currency.domain.CountryCurrency;
import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.CurrencyRepository;
import com.example.yeobee.core.trip.domain.*;
import com.example.yeobee.core.trip.dto.TripResponseDto;
import com.example.yeobee.core.trip.dto.request.CreateTripRequestDto;
import com.example.yeobee.core.trip.dto.request.UpdateTripRequestDto;
import com.example.yeobee.core.trip.dto.request.UpdateTripRequestDto.TripUserRequestDto;
import com.example.yeobee.core.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional
    public TripResponseDto createTrip(CreateTripRequestDto request, User user) {
        // create Trip
        Trip trip = new Trip(request.title(), new Period(request.startDate(), request.endDate()));

        // create TripUser and add
        trip.getTripUserList().add(new TripUser(user, trip));   // 여행 생성하는 유저
        List<TripUser> tripUsers = request.tripUserList().stream()
            .map(tu -> new TripUser(tu.name(), tu.profileImageType(), trip)).toList();
        trip.getTripUserList().addAll(tripUsers);   // 동행자

        // create TripCountry and add
        List<String> countryNames = request.countryList()
            .stream()
            .map(CreateTripRequestDto.CountryRequestDto::name)
            .toList();
        List<Country> countries = countryRepository.findAllById(countryNames);
        List<TripCountry> tripCountries = countries.stream().map(country -> new TripCountry(country, trip)).toList();
        trip.getTripCountryList().addAll(tripCountries);

        // create TripCurrency and add
        Set<Currency> currencies = new java.util.HashSet<>(
            countries.stream()
                .flatMap(country -> country.getCountryCurrencyList()
                    .stream()
                    .map(CountryCurrency::getCurrency))
                .collect(Collectors.toUnmodifiableSet()));
        List<Currency> defaultCurrencies = currencyRepository.findAllById(
            List.of("KRW", "USD"));   // 기본적으로 한화, 미국 달러 추가
        currencies.addAll(defaultCurrencies);
        List<TripCurrency> tripCurrencies = currencies.stream()
            .map(currency -> new TripCurrency(trip, currency))
            .toList();
        trip.getTripCurrencyList().addAll(tripCurrencies);

        // save Trip
        tripRepository.save(trip);

        return TripResponseDto.of(trip);
    }

    @Transactional
    public TripResponseDto updateTrip(long tripId, UpdateTripRequestDto request, User user) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));

        if (!trip.getTripUserList().stream().map(TripUser::getUser).collect(Collectors.toSet()).contains(user)) {
            throw new BusinessException(ErrorCode.TRIP_ACCESS_UNAUTHORIZED);
        }

        trip.setTitle(request.title());
        trip.setPeriod(new Period(request.startDate(), request.endDate()));

        List<TripUser> tripUsers = trip.getTripUserList();
        for (TripUser tripUser : tripUsers) {
            for (TripUserRequestDto tripUserRequest : request.tripUserList()) {
                if (tripUser.getId().equals(tripUserRequest.id())) {
                    tripUser.setName(tripUserRequest.name());
                }
            }
        }

        return TripResponseDto.of(trip);
    }

    @Transactional
    public void delete(long tripId, User user) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));

        // TODO: 삭제 권한 정책 검토
        if (!trip.getTripUserList().stream().map(TripUser::getUser).collect(Collectors.toSet()).contains(user)) {
            throw new BusinessException(ErrorCode.TRIP_ACCESS_UNAUTHORIZED);
        }

        tripRepository.delete(trip);
    }

    public TripResponseDto getTrip(long tripId, User user) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));

        if (!trip.getTripUserList().stream().map(TripUser::getUser).collect(Collectors.toSet()).contains(user)) {
            throw new BusinessException(ErrorCode.TRIP_ACCESS_UNAUTHORIZED);
        }

        return TripResponseDto.of(trip);
    }

    public Page<TripResponseDto> getPastTrips(PageRequestDto request, User user) {
        Page<Trip> trips = tripRepository.findPastTrips(user, LocalDate.now(), request.toPageRequest());
        return trips.map(TripResponseDto::of);
    }

    public Page<TripResponseDto> getPresentTrips(PageRequestDto request, User user) {
        Page<Trip> trips = tripRepository.findPresentTrips(user, LocalDate.now(), request.toPageRequest());
        return trips.map(TripResponseDto::of);
    }

    public Page<TripResponseDto> getFutureTrips(PageRequestDto request, User user) {
        Page<Trip> trips = tripRepository.findFutureTrips(user, LocalDate.now(), request.toPageRequest());
        return trips.map(TripResponseDto::of);
    }
}
