package com.cerberus.userservice.controller;

import com.cerberus.userservice.exception.ValidationException;
import com.cerberus.userservice.model.CodeVerificationRequest;
import com.cerberus.userservice.model.CreateConfirmationCodeRequest;
import com.cerberus.userservice.model.UpdatePasswordRequest;
import com.cerberus.userservice.service.ConfirmationCodeService;
import com.cerberus.userservice.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/confirmation-codes")
@RequiredArgsConstructor
public class ConfirmationCodeController {

    private final ConfirmationCodeService confirmationCodeService;

    private final MailService mailService;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestBody @Valid CodeVerificationRequest codeVerificationRequest,
                                          BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.confirmationCodeService.confirmEmail(codeVerificationRequest);
        return ResponseEntity.ok("The email has been successfully confirmed.");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
                                                 BindingResult bindingResult, HttpServletRequest httpServletRequest){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        String jwtToken = httpServletRequest.getHeader("Authorization").substring(7);
        this.confirmationCodeService.updatePassword(updatePasswordRequest, jwtToken);
        return ResponseEntity.ok("The password has been successfully changed.");
    }

    @PostMapping("/recreate")
    public ResponseEntity<String> recreate(@RequestBody @Valid CreateConfirmationCodeRequest codeRequest,
                                           BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        Integer code = this.confirmationCodeService.recreate(codeRequest);
        this.mailService.sendEmail(codeRequest,code);
        return ResponseEntity.ok("The email has been sent successfully.");
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateConfirmationCodeRequest codeRequest){
        Integer code = this.confirmationCodeService.create(codeRequest);
        this.mailService.sendEmail(codeRequest,code);
        return ResponseEntity.ok("The email has been sent successfully.");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString().replace("[", "").replace("]", "");
    }
}
