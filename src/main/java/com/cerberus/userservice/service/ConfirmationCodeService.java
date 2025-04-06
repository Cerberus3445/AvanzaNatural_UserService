package com.cerberus.userservice.service;

import com.cerberus.userservice.model.CodeVerificationRequest;
import com.cerberus.userservice.model.CreateConfirmationCodeRequest;
import com.cerberus.userservice.model.UpdatePasswordRequest;

public interface ConfirmationCodeService {

    Integer create(CreateConfirmationCodeRequest codeRequest);

    Integer recreate(CreateConfirmationCodeRequest codeRequest);

    void confirmEmail(CodeVerificationRequest codeRequest);

    void updatePassword(UpdatePasswordRequest passwordRequest, String jwtToken);
}
