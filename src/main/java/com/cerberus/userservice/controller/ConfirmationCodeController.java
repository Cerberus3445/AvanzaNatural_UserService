package com.cerberus.userservice.controller;

import com.cerberus.userservice.exception.ValidationException;
import com.cerberus.userservice.mapper.UserMapper;
import com.cerberus.userservice.model.CodeVerificationRequest;
import com.cerberus.userservice.model.CreateConfirmationCodeRequest;
import com.cerberus.userservice.model.UpdatePasswordRequest;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.service.ConfirmationCodeService;
import com.cerberus.userservice.service.MailService;
import com.cerberus.userservice.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RateLimiter(name = "confirmationCodeLimiter")
@Tag(name = "ConfirmationCode Controller", description = "Interaction with user")
public class ConfirmationCodeController {

    private final ConfirmationCodeService confirmationCodeService;

    private final MailService mailService;

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/confirm")
    @Operation(summary = "Email confirmation")
    public ResponseEntity<String> confirmEmail(@RequestBody @Valid CodeVerificationRequest codeVerificationRequest,
                                          BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.confirmationCodeService.confirmEmail(codeVerificationRequest);
        return ResponseEntity.ok("The email has been successfully confirmed.");
    }

    @PostMapping("/update-password")
    @Operation(summary = "Password updating")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
                                                 BindingResult bindingResult, HttpServletRequest httpServletRequest){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        String jwtToken = httpServletRequest.getHeader("Authorization").substring(7);
        this.confirmationCodeService.updatePassword(updatePasswordRequest, jwtToken);
        return ResponseEntity.ok("The password has been successfully changed.");
    }

    @PostMapping("/recreate")
    @Operation(summary = "Recreate a new confirmation code")
    public ResponseEntity<String> recreate(@RequestBody @Valid CreateConfirmationCodeRequest codeRequest,
                                           BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        User user = this.userMapper.toEntity(this.userService.getByEmail(codeRequest.getEmail()));

        Integer code = this.confirmationCodeService.recreate(user, codeRequest);

        this.mailService.sendEmail(user,code);
        return ResponseEntity.ok("The email has been sent successfully.");
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new confirmation code")
    public ResponseEntity<String> create(@RequestBody @Valid CreateConfirmationCodeRequest codeRequest,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        User user = this.userMapper.toEntity(this.userService.getByEmail(codeRequest.getEmail()));
        Integer code = this.confirmationCodeService.create(user, codeRequest);

        this.mailService.sendEmail(user,code);
        return ResponseEntity.ok("The email has been sent successfully.");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString().replace("[", "").replace("]", "");
    }
}
