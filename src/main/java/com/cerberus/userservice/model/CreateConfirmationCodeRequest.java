package com.cerberus.userservice.model;

import com.cerberus.userservice.dto.UserDto;
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

    @NotNull(message = "The userDto cannot be empty.")
    private UserDto userDto;
}
