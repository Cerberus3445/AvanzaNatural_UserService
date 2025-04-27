package com.cerberus.userservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConfirmationCodeRequest {

    @NotNull(message = "The confirmationCodeType field cannot be empty.")
    private Type confirmationCodeType;

    @NotNull(message = "The userId cannot be empty.")
    private Long userId;

    @NotBlank(message = "The email cannot be empty.")
    private String email;
}
