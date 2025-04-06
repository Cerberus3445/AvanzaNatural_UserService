package com.cerberus.userservice.client;

public interface IdentityClient {

    void deleteAllRefreshTokens(Long userId, String token);
}
