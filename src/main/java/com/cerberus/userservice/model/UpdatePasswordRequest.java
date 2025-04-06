package com.cerberus.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    private String newPassword;

    private String confirmPassword;

    private CodeVerificationRequest codeVerificationRequest;

}
