package com.example.yeobee.common.config;

import com.example.yeobee.common.util.UrlUtil;
import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.currency.domain.CountryCurrency;
import com.example.yeobee.core.currency.domain.Currency;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitRunner implements ApplicationRunner {

    private final EntityManager em;

    @Value("${yeobee.init:false}")
    private boolean runInit;

    @Value("${spring.cloud.aws.cdn.url}")
    private String cdnUrl;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        if (runInit) {
            init();
        }
    }

    private void init() {
        log.info("Start custom data initialization...");
        initS3();
        initDB();
    }

    private void initS3() {
        // TODO: 이미지 업로드 구현
    }

    @SneakyThrows
    private void initDB() {
        // country
        BufferedReader countryReader = Files.newBufferedReader(Paths.get(new ClassPathResource("init/db/country.csv").getURI()));
        List<Country> countries = new CsvToBeanBuilder<Country>(countryReader)
            .withType(Country.class)
            .build()
            .parse();

        Field flagImageUrlField = Country.class.getDeclaredField("flagImageUrl");
        flagImageUrlField.setAccessible(true);
        for (Country country : countries) {
            flagImageUrlField.set(country, UrlUtil.join(cdnUrl, "/static/country/flag/", country.getName() + ".svg"));
        }
        for (Country country : countries) {
            upsert(country);
        }

        // currency
        BufferedReader currencyReader = Files.newBufferedReader(Paths.get(new ClassPathResource("init/db/currency.csv").getURI()));
        List<Currency> currencies = new CsvToBeanBuilder<Currency>(currencyReader)
            .withType(Currency.class)
            .build()
            .parse();
        for (Currency currency : currencies) {
            upsert(currency);
        }

        // country_currency
        BufferedReader countryCurrencyReader = Files.newBufferedReader(Paths.get(new ClassPathResource(
            "init/db/country_currency.csv").getURI()));
        List<CountryCurrencyDto> countryCurrencies = new CsvToBeanBuilder<CountryCurrencyDto>(countryCurrencyReader)
            .withType(CountryCurrencyDto.class)
            .build()
            .parse();
        for (CountryCurrencyDto countryCurrencyDto : countryCurrencies) {
            CountryCurrency countryCurrency = new CountryCurrency(
                new Country(countryCurrencyDto.getCountryName()),
                new Currency(countryCurrencyDto.getCurrencyCode())
            );
            upsert(countryCurrency);
        }
    }

    @SneakyThrows
    private void upsert(Object entity) {
        Class<?> clazz = entity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();

        Field idField = null;
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
        }

        if (idField == null) {
            throw new RuntimeException("@Id not found - not a valid entity");
        }

        idField.setAccessible(true);
        Object id = idField.get(entity);
        if (id != null) {
            Object foundEntity = em.find(clazz, id);
            if (foundEntity != null) {
                for (Field field : declaredFields) {
                    if (!field.equals(idField)) {
                        field.setAccessible(true);
                        field.set(foundEntity, field.get(entity));
                    }
                }
            } else {
                em.persist(entity);
            }
        } else {
            em.persist(entity);
        }
    }

    @Setter
    @Getter
    public static class CountryCurrencyDto {

        private String currencyCode;
        private String countryName;
    }
}
