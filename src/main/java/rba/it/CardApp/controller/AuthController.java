package rba.it.CardApp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rba.it.CardApp.dto.TokenRequest;
import rba.it.CardApp.dto.TokenResponse;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Value("${auth0.client_id}")
    private String clientId;

    @Value("${auth0.client_secret}")
    private String clientSecret;

    @Value("${auth0.domain}")
    private String authDomain;

    @Value("${auth0.audience}")
    private String audience;

    @PostMapping("/get-token")
    public ResponseEntity<TokenResponse> getToken() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = authDomain + "/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClient_id(clientId);
        tokenRequest.setClient_secret(clientSecret);
        tokenRequest.setAudience(audience);
        tokenRequest.setGrant_type("client_credentials");

        HttpEntity<TokenRequest> request = new HttpEntity<>(tokenRequest, headers);

        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                TokenResponse.class
        );

        return response;
    }
}
