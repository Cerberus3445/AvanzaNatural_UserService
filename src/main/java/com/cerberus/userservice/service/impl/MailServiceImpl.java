package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.model.ConfirmationCode;
import com.cerberus.userservice.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;

    private final JavaMailSender mailSender;

    @Override
    @SneakyThrows(MessagingException.class)
    public void sendEmail(UserDto user, Integer code) {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setSubject("Email confirmation code for %s %s".formatted(
                user.getFirstName(), user.getLastName()));
        mimeMessageHelper.setTo(user.getEmail());
        String emailContent = getConfirmationEmailContent(user, code);
        mimeMessageHelper.setText(emailContent, true);
        this.mailSender.send(mimeMessage);
    }

    @SneakyThrows({IOException.class, TemplateException.class})
    private String getConfirmationEmailContent(UserDto user, Integer code) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> module = new HashMap<>();
        module.put("firstName", user.getFirstName());
        module.put("lastName", user.getLastName());
        module.put("code", code);
        configuration.getTemplate("confirmation.ftlh").process(module, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
