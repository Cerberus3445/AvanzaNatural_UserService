package com.cerberus.userservice.service;


import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {

    String extractEmail(String token);

    Collection<? extends GrantedAuthority> extractRole(String token);
}
