package com.example.yeobee.core.currency.scheduler;

import com.example.yeobee.core.currency.domain.CurrencyRepository;
import com.example.yeobee.core.currency.dto.response.ExchangeRateInfo;
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
    private static final String API_ENDPOINT = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?data=AP01&authKey=";

    @Scheduled
    private void updateExchangeRate(@Value("${exchangeRate.authKey}") String authKey) {
        HashSet<String> currencySet = new HashSet<>(currencyRepository.findAllCurrencyCodes()); // 메소드 이름 변경에 주의하세요.
        String requestUrl = API_ENDPOINT + authKey;

        ExchangeRateInfo[] response = restTemplate.getForObject(requestUrl,
                                                                ExchangeRateInfo[].class); // JSON 응답을 객체 배열로 변환
        if (response != null) {
            List<ExchangeRateInfo> exchangeRates = Arrays.asList(response);

            // 여기에서 exchangeRates를 사용하여 필요한 작업을 수행하세요.
            // 예: DB에 환율 정보 업데이트
        }
    }
}
