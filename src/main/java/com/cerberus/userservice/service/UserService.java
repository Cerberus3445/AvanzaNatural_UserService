package com.cerberus.userservice.service;

import com.cerberus.userservice.dto.UserDto;

import java.util.Optional;

public interface UserService {

    UserDto get(Long id);

    void create(UserDto userDto);

    void update(Long id, UserDto userDto);

    void delete(Long id);

    Optional<UserDto> getByEmail(String email);

    void updateEmailConfirmedStatus(Long userId);
}
