package com.cerberus.userservice;

import com.cerberus.userservice.client.IdentityClient;
import com.cerberus.userservice.model.*;
import com.cerberus.userservice.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfirmationCodeTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockitoBean
    private MailService mailService;

    @MockitoBean
    private IdentityClient identityClient;

    @Test
    public void create(){
        HttpEntity<CreateConfirmationCodeRequest> httpEntity = new HttpEntity<>(new CreateConfirmationCodeRequest(Type.EMAIL,
                "rayan_thompson@gmail.com"));

        doNothing().when(this.mailService).sendEmail(any(User.class), anyInt());

        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/confirmation-codes/create",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("The email has been sent successfully.", responseEntity.getBody());
    }

    @Test
    public void confirmEmail() {
        HttpEntity<CodeVerificationRequest> httpEntity = new HttpEntity<>(new CodeVerificationRequest("jeffbezos@gmail.com", 1_234_567));

        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/confirmation-codes/confirm",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("The email has been successfully confirmed.", responseEntity.getBody());
    }

    @Test
    public void recreate() {
        HttpEntity<CreateConfirmationCodeRequest> httpEntity = new HttpEntity<>(new CreateConfirmationCodeRequest(Type.EMAIL,
                "donaldtrump@gmail.com"));

        doNothing().when(this.mailService).sendEmail(any(User.class), anyInt());

        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/confirmation-codes/recreate",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("The email has been sent successfully.", responseEntity.getBody());
    }

    @Test
    public void updatePassword() {
        HttpEntity<UpdatePasswordRequest> httpEntity = new HttpEntity<>(new UpdatePasswordRequest("123456", "123456",
                new CodeVerificationRequest("paveldurovtg@gmail.com", 1_234_567)));

        doNothing().when(this.identityClient).deleteAllRefreshTokens(anyLong());

        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/confirmation-codes/update-password",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        System.out.println(responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("The password has been successfully changed.", responseEntity.getBody());
    }
}
