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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitRunner implements ApplicationRunner {

    private static final String S3_FLAG_KEY_PATH = "static/country/flag/";

    private final EntityManager em;
    private final S3Client s3Client;

    @Value("${yeobee.init:false}")
    private boolean runInit;

    @Value("${spring.cloud.aws.cdn.url}")
    private String cdnUrl;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

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
        log.info("Custom data initialization complete!");
    }

    @SneakyThrows
    private void initS3() {
        String resourcePattern = "classpath*:init/s3/country/flag/**/*.*"; // Adjust path and pattern as needed
        List<Resource> resources = listResources(resourcePattern);
        for (Resource resource : resources) {
            Path file = Path.of(resource.getURI());
            uploadFileToS3(file);
        }
    }

    @SneakyThrows
    private List<Resource> listResources(String pattern) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(pattern);
        List<Resource> fileList = new ArrayList<>();
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                fileList.add(resource);
            }
        }
        return fileList;
    }

    @SneakyThrows
    private void uploadFileToS3(Path filePath) {
        String key = UrlUtil.join(S3_FLAG_KEY_PATH, filePath.getFileName().toString());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .bucketKeyEnabled(true)
            .build();

        s3Client.putObject(putObjectRequest, filePath);

        log.info("File uploaded successfully: {}", key);
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
            flagImageUrlField.set(country, UrlUtil.join(cdnUrl, S3_FLAG_KEY_PATH, country.getName() + ".svg"));
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
