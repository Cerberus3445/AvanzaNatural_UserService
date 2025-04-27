package com.cerberus.userservice.service;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.model.CreateConfirmationCodeRequest;


public interface MailService {

    void sendEmail(CreateConfirmationCodeRequest codeRequest, Integer code);
}
