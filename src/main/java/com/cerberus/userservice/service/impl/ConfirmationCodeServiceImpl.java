package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.client.IdentityClient;
import com.cerberus.userservice.exception.NotFoundException;
import com.cerberus.userservice.exception.ValidationException;
import com.cerberus.userservice.model.*;
import com.cerberus.userservice.repository.ConfirmationCodeRepository;
import com.cerberus.userservice.service.ConfirmationCodeService;
import com.cerberus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

    private final ConfirmationCodeRepository confirmationCodeRepository;

    private final UserService userService;

    private final static Integer EXPIRATION = 24;

    private final IdentityClient identityClient;

    private final Random random = new Random();

    @Override
    public Integer create(User user, CreateConfirmationCodeRequest codeRequest) {
        Integer countConfirmationCode = 0;

        switch(codeRequest.getConfirmationCodeType()){
            case EMAIL -> countConfirmationCode += this.confirmationCodeRepository.countByTypeAndUser_Id(Type.EMAIL, user.getId());
            case PASSWORD -> countConfirmationCode += this.confirmationCodeRepository.countByTypeAndUser_Id(Type.PASSWORD, user.getId());
            default -> throw new ValidationException("The confirmationCodeType is invalid.");
        }

        if(countConfirmationCode >= 1){ //The capacities allow you to send only 1 email
            throw new ValidationException("The email has already been sent. If you haven't received it, then recreate it.");
        }

        checkTheEmailConfirmation(user, codeRequest);

        ConfirmationCode confirmationCode = this.confirmationCodeRepository.save(new ConfirmationCode(
                null, this.random.nextInt(1_000_000, 9_999_999),LocalDateTime.now().plusHours(EXPIRATION),codeRequest.getConfirmationCodeType(),user
        ));

        return confirmationCode.getCode();
    }

    @Override
    public Integer recreate(User user, CreateConfirmationCodeRequest codeRequest) {
        this.confirmationCodeRepository.deleteByUser_Id(user.getId());

        checkTheEmailConfirmation(user, codeRequest);

        ConfirmationCode confirmationCode = this.confirmationCodeRepository.save(new ConfirmationCode(
                null, this.random.nextInt(1_000_000, 9_999_999),LocalDateTime.now().plusHours(EXPIRATION),codeRequest.getConfirmationCodeType(),user
        ));

        return confirmationCode.getCode();
    }

    @Override
    public void confirmEmail(CodeVerificationRequest code) {
        ConfirmationCode confirmationCode = this.confirmationCodeRepository.findByTypeAndUser_Email(Type.EMAIL, code.getEmail())
                .orElseThrow(() -> new NotFoundException("Code not found"));

        if(Objects.equals(confirmationCode.getCode(), code.getCode()) && LocalDateTime.now().isBefore(confirmationCode.getExpirationDate())){
            this.userService.updateEmailConfirmedStatus(code.getEmail());
            this.confirmationCodeRepository.deleteById(confirmationCode.getId());
        } else {
            throw new ValidationException("The confirmation code is not valid.");
        }
    }

    @Override
    public void updatePassword(UpdatePasswordRequest passwordRequest, String jwtToken) {
        ConfirmationCode confirmationCode =  this.confirmationCodeRepository.findByTypeAndUser_Email(Type.PASSWORD,
                        passwordRequest.getCodeVerificationRequest().getEmail())
                .orElseThrow(() -> new NotFoundException(passwordRequest.getCodeVerificationRequest().getEmail()));

        if(!Objects.equals(confirmationCode.getCode(), passwordRequest.getCodeVerificationRequest().getCode()) ||
                LocalDateTime.now().isAfter(confirmationCode.getExpirationDate())){
            throw new ValidationException("The confirmation code is incorrect.");
        }
        if(!Objects.equals(passwordRequest.getNewPassword(), passwordRequest.getConfirmPassword())){
            throw new ValidationException("Passwords don't match.");
        }

        this.userService.updatePassword(passwordRequest.getCodeVerificationRequest().getEmail(),passwordRequest.getNewPassword());
        this.confirmationCodeRepository.deleteByCode(confirmationCode.getCode());
        this.identityClient.deleteAllRefreshTokens(confirmationCode.getUser().getId(), jwtToken);
    }

    private void checkTheEmailConfirmation(User user, CreateConfirmationCodeRequest codeRequest){
        if(user.getEmailConfirmed() && codeRequest.getConfirmationCodeType() == Type.EMAIL){
            throw new ValidationException("The email was confirmed");
        }
    }
}
