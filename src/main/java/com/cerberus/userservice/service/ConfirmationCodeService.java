package com.cerberus.userservice.service;

import com.cerberus.userservice.dto.UserDto;

public interface ConfirmationCodeService {

    Integer create(UserDto userDto);

    Integer recreate(UserDto userDto);

    void confirm(UserDto userDto, Integer code);
}
