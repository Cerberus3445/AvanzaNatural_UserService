package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.NotFoundException;
import com.cerberus.userservice.mapper.EntityDtoMapper;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.repository.UserRepository;
import com.cerberus.userservice.service.ConfirmationCodeService;
import com.cerberus.userservice.service.MailService;
import com.cerberus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EntityDtoMapper mapper;

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserDto get(Long id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    @Transactional
    public void create(UserDto userDto) {
        log.info("create {}", userDto);
        userDto.setEmailConfirmed(false);
        this.userRepository.save(this.mapper.toEntity(userDto));
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    @Transactional
    public void update(Long id, UserDto userDto) {
        log.info("update {}, {}", id, userDto);
        this.userRepository.findById(id).ifPresentOrElse(user -> {
            User newUser = User.builder()
                    .id(user.getId())
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .password(userDto.getPassword())
                    .emailConfirmed(user.getEmailConfirmed())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            this.userRepository.save(newUser);
        }, () -> {
            throw new NotFoundException(id);
        });
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    @Transactional
    public void delete(Long id) {
        log.info("delete {}", id);
        this.userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> getByEmail(String email) {
        return this.userRepository.findByEmail(email).map(this.mapper::toDto);
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    @Transactional
    public void updateEmailConfirmedStatus(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        user.setEmailConfirmed(true);
    }
}
