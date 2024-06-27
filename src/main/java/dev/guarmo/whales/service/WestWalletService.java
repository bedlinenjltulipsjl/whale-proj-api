package dev.guarmo.whales.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Locale;

@Service
@Slf4j
public class WestWalletService {
    @Value("${westwallet.apikey}")
    private String apiKey;
    @Value("${westwallet.listener.link}")
    private String linkToApiListener;
    @Value("${westwallet.secretkey}")
    private String secretKeyString;

    public String generateInvoicePagePostRequestResponse(String userLogin, String cryptoCurrencyType, Double topUpAmount) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String timestamp = String.valueOf(Instant.now().getEpochSecond());; // Replace with your actual timestamp

            // URL and JSON data for creating invoice
            String url = "https://api.westwallet.io/address/create_invoice";
            String json = """
                {"currencies": ["%s"], "amount": "%.2f", "ipn_url": "%s", "success_url": "https://www.youtube.com/", "ttl": 15, "label": "%s"}""";
            json = String.format(Locale.US, json, cryptoCurrencyType, topUpAmount, linkToApiListener, userLogin);

            return sendRequestWithCertainParams(timestamp, json, url, httpClient);
        } catch (Exception e) {
            log.error("Error in generate invoice link", e);
            throw new RuntimeException("Error in generate invoice link", e);
        }
    }

    public String getInfoForIncomeTransaction(Integer transactionId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String timestamp = String.valueOf(Instant.now().getEpochSecond());; // Replace with your actual timestamp

            // URL and JSON data for creating invoice
            String url = "https://api.westwallet.io/wallet/transaction";
            String json = "{\"id\": %d}";
            json = String.format(Locale.US, json, transactionId);

            return sendRequestWithCertainParams(timestamp, json, url, httpClient);
        } catch (Exception e) {
            log.error("Error in generate invoice link", e);
            throw new RuntimeException("Error in generate invoice link", e);
        }
    }

    private String sendRequestWithCertainParams(String timestamp, String json, String url, CloseableHttpClient httpClient) throws Exception {
        String dataToEncodeForHeader = timestamp + json;

        byte[] encodedBytes1 = secretKeyString.getBytes(StandardCharsets.UTF_8);
        byte[] encodedBytes2 = dataToEncodeForHeader.getBytes(StandardCharsets.UTF_8);
        String sign = calculateHMAC(encodedBytes1, encodedBytes2);

        // Create a POST request
        HttpPost postRequest = new HttpPost(url);

        // Set headers (optional)
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("X-API-KEY", apiKey);
        postRequest.addHeader("X-ACCESS-SIGN", sign);
        postRequest.addHeader("X-ACCESS-TIMESTAMP", timestamp);

        StringEntity entity = new StringEntity(json);
        postRequest.setEntity(entity);

        // Execute the request
        try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseBody = EntityUtils.toString(responseEntity);
                log.info("Response Body and Request Body after sending POST request to get invoice link: {} {} {}", statusCode, responseBody, json);
                return responseBody;
            } else {
                log.error("Response Entity is null after sending POST request to get invoice link: {} {} {}", statusCode, response, json);
                throw new Exception("Response entity is null");
            }
        }
    }

    private String calculateHMAC(byte[] key, byte[] message) {
        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            log.error("Error, algorithm NOT found for HMAC", e);
            throw new RuntimeException("Algorithm NOT found exception: ", e);
        }
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        try {
            sha256_HMAC.init(secretKey);
        } catch (InvalidKeyException e) {
            log.error("Error initiation with secret key HMAC", e);
            throw new RuntimeException("Initiation HMAC with secret key error: ", e);
        }
        byte[] hash = sha256_HMAC.doFinal(message);
        return Hex.encodeHexString(hash);
    }
}
