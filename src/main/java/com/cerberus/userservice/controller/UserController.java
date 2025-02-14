package com.cerberus.userservice.controller;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.NotFoundException;
import com.cerberus.userservice.exception.ValidationException;
import com.cerberus.userservice.service.UserService;
import com.cerberus.userservice.validator.CreateValidator;
import com.cerberus.userservice.validator.UpdateValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final CreateValidator createValidator;

    private final UpdateValidator updateValidator;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id){
        return this.userService.get(id);
    }

    @GetMapping("/email/{email}")
    public UserDto getByEmail(@PathVariable("email") String email){
         return this.userService.getByEmail(email).orElseThrow(() ->
                 new NotFoundException(email));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        this.createValidator.validate(userDto);

        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User has been created.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        this.updateValidator.validate(userDto);

        if(bindingResult.hasFieldErrors()) throw new ValidationException(collectErrorsToString(bindingResult.getFieldErrors()));

        this.userService.update(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body("User has been updated.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        this.userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("User has been deleted.");
    }

    private String collectErrorsToString(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString();
    }
}
