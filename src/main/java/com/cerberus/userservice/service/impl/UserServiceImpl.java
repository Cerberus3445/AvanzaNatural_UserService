package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.cache.CacheClear;
import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.NotFoundException;
import com.cerberus.userservice.mapper.UserMapper;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.repository.UserRepository;
import com.cerberus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CacheClear cacheClear;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserDto get(Long id) {
        log.info("get {}", id);
        return this.userMapper.toDto(this.userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id))
        );
    }

    @Override
    @Transactional
    public void create(UserDto userDto) {
        log.info("create {}", userDto);
        userDto.setEmailConfirmed(false);
        this.userRepository.save(this.userMapper.toEntity(userDto));
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#id")
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
    @Transactional
    @CacheEvict(value = "user", key = "#id")
    public void delete(Long id) {
        log.info("delete {}", id);
        this.userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id)
        );

        this.userRepository.deleteById(id);
    }

    @Override
    public UserDto getByEmail(String email) {
        return this.userMapper.toDto(this.userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(email)
        ));
    }

    @Override
    @Transactional
    public void updateEmailConfirmedStatus(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(email));
        user.setEmailConfirmed(true);

        this.cacheClear.clearUserById(user.getId());
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(email));
        user.setPassword(this.passwordEncoder.encode(newPassword));

        this.cacheClear.clearUserById(user.getId());
    }
}

