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

    /**
     * The method creates a new confirmation code for a user.
     * The confirmation code is unique for each user and has a type.
     * The method checks if the user has already confirmation code with the same type.
     * If the user has a confirmation code with the same type, the method throws ValidationException.
     * The method saves the confirmation code and returns the confirmation code.
     */
    @Override
    public Integer create(User user, CreateConfirmationCodeRequest codeRequest) {
        Integer countOfConfirmationCode = countCodesByTypeAndUser(user, codeRequest);

        if(countOfConfirmationCode >= 1){ //The capacities allow you to send only 1 email
            throw new ValidationException("The email has already been sent. If you haven't received it, then recreate it.");
        }

        checkTheEmailConfirmation(user, codeRequest);

        ConfirmationCode confirmationCode = this.confirmationCodeRepository.save(new ConfirmationCode(
                null, this.random.nextInt(1_000_000, 9_999_999),LocalDateTime.now().plusHours(EXPIRATION),codeRequest.getConfirmationCodeType(),user
        ));

        return confirmationCode.getCode();
    }

    /**
     * The method recreates a confirmation code for a user.
     * The confirmation code is unique for each user and has a type.
     * The method checks if the user has already confirmation code with the same type.
     * If the user has a confirmation code with the same type, the method deletes the old confirmation code and creates a new one.
     * If the user doesn't have a confirmation code with the same type, the method throws ValidationException.
     * The method saves the confirmation code and returns the confirmation code.
     */
    @Override
    public Integer recreate(User user, CreateConfirmationCodeRequest codeRequest) {
        Integer countOfConfirmationCode = countCodesByTypeAndUser(user, codeRequest);

        if(countOfConfirmationCode  == 0){ //The capacities allow you to send only 1 email
            throw new ValidationException("You couldn't recreate the code because no emails were sent to your email.");
        }

        this.confirmationCodeRepository.deleteByTypeAndUser_Id(codeRequest.getConfirmationCodeType(), user.getId());

        checkTheEmailConfirmation(user, codeRequest);

        ConfirmationCode confirmationCode = this.confirmationCodeRepository.save(new ConfirmationCode(
                null, this.random.nextInt(1_000_000, 9_999_999), LocalDateTime.now().plusHours(EXPIRATION),
                codeRequest.getConfirmationCodeType(), user
        ));

        return confirmationCode.getCode();
    }

    /**
     * The method confirms the user's email.
     * The method checks if the code verification request is valid.
     * If the code verification request is valid, the method updates the user's email confirmed status and deletes the confirmation code.
     * If the code verification request is not valid, the method throws ValidationException.
     */
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

    /**
     * The method updates the user's password.
     * The method checks if the code verification request is valid.
     * If the code verification request is valid, the method updates the user's password and deletes the confirmation code.
     * If the code verification request is not valid, the method throws ValidationException.
     * The method also deletes all refresh tokens of the user.
     * @param passwordRequest the request with new password and confirmation code.
     */
    @Override
    public void updatePassword(UpdatePasswordRequest passwordRequest) {
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
        this.identityClient.deleteAllRefreshTokens(confirmationCode.getUser().getId());
    }

    /**
     * The method checks if the user has already confirmed his email.
     * If the user has already confirmed his email and the confirmation code type is EMAIL,
     * the method throws ValidationException.
     * @param user the user to check.
     * @param codeRequest the request with confirmation code type.
     */
    private void checkTheEmailConfirmation(User user, CreateConfirmationCodeRequest codeRequest){
        if(user.getEmailConfirmed() && codeRequest.getConfirmationCodeType() == Type.EMAIL){
            throw new ValidationException("The email was confirmed");
        }
    }

    /**
     * The method counts the number of confirmation codes of the user by type.
     * The method returns the count of confirmation codes.
     * @param user the user to count confirmation codes.
     * @param codeRequest the request with confirmation code type.
     * @return the count of confirmation codes.
     */
    private Integer countCodesByTypeAndUser(User user, CreateConfirmationCodeRequest codeRequest){
        Integer count = 0;
        switch(codeRequest.getConfirmationCodeType()){
            case EMAIL -> count += this.confirmationCodeRepository.countByTypeAndUser_Id(Type.EMAIL, user.getId());
            case PASSWORD -> count += this.confirmationCodeRepository.countByTypeAndUser_Id(Type.PASSWORD, user.getId());
            default -> throw new ValidationException("The confirmationCodeType is invalid.");
        }
        return count;
    }
}