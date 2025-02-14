package com.cerberus.userservice.service;

import com.cerberus.userservice.dto.UserDto;


public interface MailService {

    void sendEmail(UserDto user, Integer code);
}
