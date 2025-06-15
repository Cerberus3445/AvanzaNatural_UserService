package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.model.User;
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
import org.springframework.scheduling.annotation.Async;
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
    @Async("asyncTaskExecutor")
    @SneakyThrows(MessagingException.class)
    public void sendEmail(User user, Integer code) {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setSubject("Avanza Natural");
        String emailContent = getConfirmationEmailContent(user, code);
        mimeMessageHelper.setText(emailContent, true);
        this.mailSender.send(mimeMessage);
    }

    @SneakyThrows({IOException.class, TemplateException.class})
    private String getConfirmationEmailContent(User user, Integer code) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> module = new HashMap<>();
        module.put("firstName", user.getFirstName());
        module.put("lastName", user.getLastName());
        module.put("code", code);
        configuration.getTemplate("confirmation.ftlh").process(module, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
