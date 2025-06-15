package com.cerberus.userservice.client.impl;

import com.cerberus.userservice.client.IdentityClient;
import com.cerberus.userservice.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class IdentityClientImpl implements IdentityClient {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://localhost:9090/api/v1/auth")
            .build();

    @Override
    @CircuitBreaker(name = "identityClient", fallbackMethod = "fallback")
    public void deleteAllRefreshTokens(Long userId) {
        String path = "/delete-all-refresh-tokens";
        String query = "userId={userId}";
        this.restClient.delete()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(userId))
                .retrieve()
                .body(String.class);
    }

    public void fallback(Long userId, Throwable t) {
         throw new ServiceUnavailableException();
    }
}
