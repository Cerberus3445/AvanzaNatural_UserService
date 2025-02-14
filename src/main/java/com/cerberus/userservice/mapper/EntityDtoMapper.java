package com.cerberus.userservice.mapper;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

}
