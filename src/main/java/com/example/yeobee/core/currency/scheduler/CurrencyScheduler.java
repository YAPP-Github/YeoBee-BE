package com.example.yeobee.core.currency.scheduler;

import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.CurrencyRepository;
import com.example.yeobee.core.currency.dto.response.ExchangeRateInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CurrencyScheduler {

    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;
    @Value("${exchangeRate.authKey}")
    private static String AUTH_KEY;
    private static final String API_ENDPOINT = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?data=AP01&authKey=";

    @Scheduled
    private void updateExchangeRate() {
        HashSet<String> currencySet = new HashSet<>(currencyRepository.findAllCurrencyCodes());
        String requestUrl = API_ENDPOINT + AUTH_KEY;
        ExchangeRateInfo[] response = restTemplate.getForObject(requestUrl,
                                                                ExchangeRateInfo[].class);
        if (response != null) {
            List<ExchangeRateInfo> exchangeRates = Arrays.asList(response);
            List<Currency> currencyList = new ArrayList<>();
            exchangeRates.forEach((e) -> {
                if (currencySet.contains(e.cur_unit())) {
                    currencyList.add(new Currency(e.cur_unit(), e.cur_nm(), new BigDecimal(e.deal_bas_r())));
                }
            });
            currencyRepository.saveAll(currencyList);
        }
    }
}
