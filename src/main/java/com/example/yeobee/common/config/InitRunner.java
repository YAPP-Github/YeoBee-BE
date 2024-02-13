package com.example.yeobee.common.config;

import com.example.yeobee.common.util.UrlUtil;
import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.country.domain.CountryRepository;
import com.example.yeobee.core.currency.domain.CountryCurrency;
import com.example.yeobee.core.currency.domain.CountryCurrencyRepository;
import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.CurrencyRepository;
import com.opencsv.bean.CsvToBeanBuilder;
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
    private static final String S3_COVER_KEY_PATH = "static/country/cover/";
    private static final String S3_USER_PROFILE_KEY_PATH = "static/user/profile/";

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final CountryCurrencyRepository countryCurrencyRepository;
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
        for (Resource resource : listResources("classpath*:init/s3/country/flag/**/*.*")) {
            Path file = Path.of(resource.getURI());
            uploadFileToS3(S3_FLAG_KEY_PATH, file);
        }
        for (Resource resource : listResources("classpath*:init/s3/country/cover/**/*.*")) {
            Path file = Path.of(resource.getURI());
            uploadFileToS3(S3_COVER_KEY_PATH, file);
        }
        for (Resource resource : listResources("classpath*:init/s3/user/profile/**/*.*")) {
            Path file = Path.of(resource.getURI());
            uploadFileToS3(S3_USER_PROFILE_KEY_PATH, file);
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
    private void uploadFileToS3(String keyPath, Path filePath) {
        String key = UrlUtil.join(keyPath, filePath.getFileName().toString());

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
        Field coverImageUrlField = Country.class.getDeclaredField("coverImageUrl");
        flagImageUrlField.setAccessible(true);
        coverImageUrlField.setAccessible(true);

        for (Resource resource : listResources("classpath*:init/s3/country/flag/**/*.*")) {
            String flagFileName = resource.getFilename();
            for (Country country : countries) {
                if ((country.getName() + ".png").equals(flagFileName)) {
                    flagImageUrlField.set(country, UrlUtil.join(cdnUrl, S3_FLAG_KEY_PATH, flagFileName));
                }
            }
        }
        for (Resource resource : listResources("classpath*:init/s3/country/cover/**/*.*")) {
            String coverFileName = resource.getFilename();
            for (Country country : countries) {
                if ((country.getName() + ".jpg").equals(coverFileName)) {
                    coverImageUrlField.set(country, UrlUtil.join(cdnUrl, S3_COVER_KEY_PATH, coverFileName));
                }
            }
        }

        for (Country country : countries) {
            log.info("upsert country: {}", country.getName());
            countryRepository.save(country);
        }

        // currency
        BufferedReader currencyReader = Files.newBufferedReader(Paths.get(new ClassPathResource("init/db/currency.csv").getURI()));
        List<Currency> currencies = new CsvToBeanBuilder<Currency>(currencyReader)
            .withType(Currency.class)
            .build()
            .parse();
        for (Currency currency : currencies) {
            log.info("upsert currency: {}", currency.getCode());
            currencyRepository.save(currency);
        }

        // country_currency
        BufferedReader countryCurrencyReader = Files.newBufferedReader(Paths.get(new ClassPathResource(
            "init/db/country_currency.csv").getURI()));
        List<CountryCurrencyDto> countryCurrencies = new CsvToBeanBuilder<CountryCurrencyDto>(countryCurrencyReader)
            .withType(CountryCurrencyDto.class)
            .build()
            .parse();
        for (CountryCurrencyDto countryCurrencyDto : countryCurrencies) {
            CountryCurrency countryCurrency = countryCurrencyRepository.findByCountryNameAndCurrencyCode(
                    countryCurrencyDto.getCountryName(),
                    countryCurrencyDto.getCurrencyCode())
                .orElseGet(() -> new CountryCurrency(
                    new Country(countryCurrencyDto.getCountryName()),
                    new Currency(countryCurrencyDto.getCurrencyCode())));

            log.info("insert country currency: {}, {}",
                     countryCurrencyDto.getCountryName(),
                     countryCurrencyDto.getCurrencyCode());
            countryCurrencyRepository.save(countryCurrency);
        }
    }

    @Setter
    @Getter
    public static class CountryCurrencyDto {

        private String currencyCode;
        private String countryName;
    }
}
