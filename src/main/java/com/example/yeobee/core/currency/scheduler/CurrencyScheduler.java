package com.example.yeobee.core.currency.scheduler;

import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.CurrencyRepository;
import com.example.yeobee.core.currency.dto.response.CurrencyDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyScheduler {

    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;
    @Value("${exchangeRate.accessKey}")
    private String accessKey;
    private static final String API_ENDPOINT = "http://apilayer.net/api/live?source=KRW&access_key=";

    @Scheduled(initialDelay = 10)
    private void updateExchangeRate() {
        log.info("Updating exchange rate started");
        List<Currency> retrievedcurrencyList = currencyRepository.findAll();
        List<String> currencyCodes = retrievedcurrencyList.stream().map(Currency::getCode).toList();
        HashMap<String, String> currencyMap = new HashMap<>();
        retrievedcurrencyList.forEach((e) -> currencyMap.put(e.getCode(), e.getName()));

        String url = API_ENDPOINT + accessKey + "&currencies=";
        url += String.join(",", currencyCodes);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<CurrencyDto> currencyDtos = new ArrayList<>();

        if (response != null && (Boolean) response.get("success")) {
            log.info("Api Call Success");
            Map<String, Double> quotes = (Map<String, Double>) response.get("quotes");
            for (Map.Entry<String, Double> entry : quotes.entrySet()) {
                String code = entry.getKey().substring(3);
                BigDecimal convertedVal = BigDecimal.ONE.divide(BigDecimal.valueOf(entry.getValue()),
                                                                5,
                                                                RoundingMode.HALF_UP);
                currencyDtos.add(new CurrencyDto(code, currencyMap.get(code), convertedVal));
            }

            List<Currency> currencyList = currencyDtos.stream().map(Currency::new).toList();
            currencyRepository.saveAll(currencyList);
            log.info("Save Success");
        }
    }
}
