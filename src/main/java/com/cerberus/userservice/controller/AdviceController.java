package com.cerberus.userservice.controller;

import com.cerberus.userservice.exception.AlreadyExistsException;
import com.cerberus.userservice.exception.AuthorizationException;
import com.cerberus.userservice.exception.NotFoundException;
import com.cerberus.userservice.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleException(NotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        problemDetail.setTitle("User not found");
        return problemDetail;
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleException(ValidationException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage()
        );
        problemDetail.setTitle("Validation exception");
        return problemDetail;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail handleException(AlreadyExistsException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage()
        );
        problemDetail.setTitle("User already exists");
        return problemDetail;
    }

    @ExceptionHandler(AuthorizationException.class)
    public ProblemDetail handleException(AuthorizationException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, exception.getMessage()
        );
        problemDetail.setTitle("Unauthorized");
        return problemDetail;
    }
}
