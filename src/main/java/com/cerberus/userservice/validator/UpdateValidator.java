package com.cerberus.userservice.validator;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.AlreadyExistsException;
import com.cerberus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateValidator {

    private final UserService userService;

    public void validate(UserDto userDto){
        Optional<UserDto> foundUser = this.userService.getByEmail(userDto.getEmail());

        if(foundUser.isPresent() && !Objects.equals(userDto.getId(), foundUser.get().getId())
                && userDto.getEmail().equalsIgnoreCase(foundUser.get().getEmail())){
            throw new AlreadyExistsException("User with this email already exists.");
        }
    }
}
