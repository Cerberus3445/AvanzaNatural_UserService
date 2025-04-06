package com.cerberus.userservice.client.impl;

import com.cerberus.userservice.client.IdentityClient;
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
    public void deleteAllRefreshTokens(Long userId, String token) {
        String path = "/delete-all-refresh-tokens";
        String query = "userId={userId}";
        this.restClient.delete()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(userId))
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .body(String.class);
    }
}
