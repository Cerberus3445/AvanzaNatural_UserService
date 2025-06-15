package com.cerberus.userservice.service;

import com.cerberus.userservice.model.CodeVerificationRequest;
import com.cerberus.userservice.model.CreateConfirmationCodeRequest;
import com.cerberus.userservice.model.UpdatePasswordRequest;
import com.cerberus.userservice.model.User;

public interface ConfirmationCodeService {

    Integer create(User user, CreateConfirmationCodeRequest codeRequest);

    Integer recreate(User user ,CreateConfirmationCodeRequest codeRequest);

    void confirmEmail(CodeVerificationRequest codeRequest);

    void updatePassword(UpdatePasswordRequest passwordRequest);
}
