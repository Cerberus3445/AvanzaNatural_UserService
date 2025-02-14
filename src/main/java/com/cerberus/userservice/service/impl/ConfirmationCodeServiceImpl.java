package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.NotFoundException;
import com.cerberus.userservice.exception.ValidationException;
import com.cerberus.userservice.mapper.EntityDtoMapper;
import com.cerberus.userservice.model.ConfirmationCode;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.repository.ConfirmationCodeRepository;
import com.cerberus.userservice.service.ConfirmationCodeService;
import com.cerberus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

    private final ConfirmationCodeRepository confirmationCodeRepository;

    private final EntityDtoMapper mapper;

    private final UserService userService;

    @Override
    public Integer create(UserDto userDto) {
        Random random = new Random();
        User user = this.mapper.toEntity(userDto);
        ConfirmationCode confirmationCode = this.confirmationCodeRepository.save(new ConfirmationCode(
                null, random.nextInt(1_000_000),user
        ));
        return confirmationCode.getCode();
    }

    @Override
    public Integer recreate(UserDto userDto) {
        this.confirmationCodeRepository.deleteByUser_Id(userDto.getId());

        Random random = new Random();
        ConfirmationCode confirmationCode = this.confirmationCodeRepository.save(new ConfirmationCode(
                null, random.nextInt(1_000_000),this.mapper.toEntity(userDto)
        ));
        return confirmationCode.getCode();
    }

    @Override
    public void confirm(UserDto userDto, Integer code) {
        ConfirmationCode confirmationCode = this.confirmationCodeRepository.findByUser_Id(userDto.getId())
                .orElseThrow(() -> new NotFoundException(userDto.getId()));

        if(Objects.equals(confirmationCode.getCode(), code)){
            this.userService.updateEmailConfirmedStatus(userDto.getId());
            this.confirmationCodeRepository.deleteById(confirmationCode.getId());
        } else {
            throw new ValidationException("The confirmation code is not valid.");
        }
    }
}
