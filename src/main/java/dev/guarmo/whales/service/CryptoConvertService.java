package dev.guarmo.whales.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.guarmo.whales.model.currency.CryptoCurrency;
import dev.guarmo.whales.model.currency.CryptoCurrencyType;
import dev.guarmo.whales.repository.CryptoCurrencyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CryptoConvertService {
    private final CryptoCurrencyRepo cryptoCurrencyRepo;
    @Value("${convert.api.key}")
    private String apiKey;

    private final List<CryptoCurrency> cryptoCurrenciesUUIDsForApi = List.of(
            CryptoCurrency.builder().type(CryptoCurrencyType.BTC.name()).code("Qwsogvtv82FCd").build(),
            CryptoCurrency.builder().type(CryptoCurrencyType.ETH.name()).code("razxDUgYGNAdQ").build(),
            CryptoCurrency.builder().type(CryptoCurrencyType.TON.name()).code("67YlI0K1b").build(),
            CryptoCurrency.builder().type(CryptoCurrencyType.BNB.name()).code("WcwrkfNI4FUAe").build(),
            CryptoCurrency.builder().type(CryptoCurrencyType.DASH.name()).code("C9DwH-T7MEGmo").build(),
            CryptoCurrency.builder().type(CryptoCurrencyType.USDT.name()).code("HIVsRcGKkPFtW").build()
    );

    public List<CryptoCurrency> fillWithBaseData() {
        return cryptoCurrencyRepo.saveAll(cryptoCurrenciesUUIDsForApi);
    }

    public CryptoCurrency getCoinPriceInUsd(String coinType) {
        return cryptoCurrencyRepo.findCryptoCurrencyByType(coinType).orElseThrow();
    }

    public List<CryptoCurrency> updateCurrenciesTable() {
        List<CryptoCurrency> allCurrencies = cryptoCurrencyRepo.findAll();
        allCurrencies.forEach(
                m -> m.setPriceInUsd(
                        getLastCurrencyPriceFromRequest(m.getCode())
                ));
        return cryptoCurrencyRepo.saveAll(allCurrencies);
    }

    private Double getLastCurrencyPriceFromRequest(String code) {
        String lastCurrencyPriceRequest = getLastCurrencyPriceRequest(code);
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(lastCurrencyPriceRequest);
            String price = jsonNode.path("data").path("price").asText();
            return Double.parseDouble(price);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private String getLastCurrencyPriceRequest(String code) {
        // Create an instance of CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Define the URL and the request
        HttpGet request = new HttpGet("https://api.coinranking.com/v2/coin/" + code + "/price");

        // Add the required headers
        request.addHeader("x-access-token", apiKey);

        try {
            // Execute the request
            CloseableHttpResponse response = httpClient.execute(request);

            // Get the response status code
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("Response Code: {}", statusCode);

            // If the response code is 200 (HTTP_OK), read the response
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new RuntimeException("Requesting currency from API failed: ");
                }
                return EntityUtils.toString(entity);
            } else {
                log.error("Requesting currency from API failed: {}", code);
                throw new RuntimeException("Requesting currency from API failed: ");
            }
        } catch (Exception e) {
            log.error("Exception while converting: ", e);
            throw new RuntimeException("Exception while converting: ", e);
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("Exception closing HTTP connection: ", e);
            }
        }
    }
}
