package com.cerberus.userservice.mapper;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mappable<User, UserDto>{

    private final ModelMapper modelMapper;

    @Override
    public User toEntity(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

    @Override
    public UserDto toDto(User userCredential) {
        return this.modelMapper.map(userCredential, UserDto.class);
    }
}
