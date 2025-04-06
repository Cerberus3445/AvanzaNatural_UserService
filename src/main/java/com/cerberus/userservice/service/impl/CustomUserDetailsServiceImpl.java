package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.exception.NotFoundException;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.model.UserCredential;
import com.cerberus.userservice.repository.UserRepository;
import com.cerberus.userservice.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository; //if we take the UserService, it will be Despite circular references

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        return new UserCredential(user);
    }
}
