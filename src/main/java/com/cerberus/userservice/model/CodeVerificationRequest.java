package com.cerberus.userservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeVerificationRequest {

    @NotNull(message = "The userId field cannot be empty.")
    private Long userId;

    @NotNull(message = "The code field cannot be empty.")
    private Integer code;
}
