package com.cerberus.userservice.validator;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.AlreadyExistsException;
import com.cerberus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateValidator {

    private final UserService userService;

    public void validate(UserDto userDto){
        if(this.userService.getByEmail(userDto.getEmail()).isPresent()){
            throw new AlreadyExistsException("User with this email already exists.");
        }
    }

}
