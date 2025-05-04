package com.cerberus.userservice.service;

import com.cerberus.userservice.model.User;


public interface MailService {

    void sendEmail(User user, Integer code);
}
