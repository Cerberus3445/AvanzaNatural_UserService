package com.cerberus.userservice.service;

import com.cerberus.userservice.dto.UserDto;

public interface UserService {

    UserDto get(Long id);

    void create(UserDto userDto);

    void update(Long id, UserDto userDto);

    void delete(Long id);

    UserDto getByEmail(String email);

    void updateEmailConfirmedStatus(String email);

    void updatePassword(String email, String newPassword);
}
