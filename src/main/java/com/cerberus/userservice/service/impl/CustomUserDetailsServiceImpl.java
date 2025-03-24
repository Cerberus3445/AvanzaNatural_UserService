package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.mapper.UserMapper;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.model.UserCredential;
import com.cerberus.userservice.service.CustomUserDetailsService;
import com.cerberus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userMapper.toEntity(this.userService.getByEmail(username));
        return new UserCredential(user);
    }
}
