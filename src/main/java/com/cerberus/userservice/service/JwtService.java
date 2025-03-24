package com.cerberus.userservice.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface JwtService {

    Boolean validateToken(String token, UserDetails userDetails);

    String extractEmail(String token);

    Collection<? extends GrantedAuthority> extractRole(String token);
}
