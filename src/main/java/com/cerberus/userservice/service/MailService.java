package com.cerberus.userservice.service;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.model.CreateConfirmationCodeRequest;
import com.cerberus.userservice.model.User;


public interface MailService {

    void sendEmail(User user, Integer code);
}
