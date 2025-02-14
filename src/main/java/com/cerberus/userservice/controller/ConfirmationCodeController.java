package com.cerberus.userservice.controller;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.service.ConfirmationCodeService;
import com.cerberus.userservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/confirmation-codes")
@RequiredArgsConstructor
public class ConfirmationCodeController {

    private final ConfirmationCodeService confirmationCodeService;

    private final MailService mailService;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestBody UserDto userDto,
                                          @RequestParam("code") Integer code){
        this.confirmationCodeService.confirm(userDto, code);
        return ResponseEntity.ok("The email has been successfully confirmed.");
    }

    @PostMapping("/recreate")
    public ResponseEntity<String> recreate(@RequestBody UserDto userDto){
        Integer code = this.confirmationCodeService.recreate(userDto);
        this.mailService.sendEmail(userDto,code);
        return ResponseEntity.ok("The email has been sent successfully.");
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody UserDto userDto){
        Integer code = this.confirmationCodeService.create(userDto);
        this.mailService.sendEmail(userDto,code);
        return ResponseEntity.ok("The email has been sent successfully.");
    }
}
