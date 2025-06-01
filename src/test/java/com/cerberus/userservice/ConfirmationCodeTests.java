package com.cerberus.userservice;

import com.cerberus.userservice.model.CreateConfirmationCodeRequest;
import com.cerberus.userservice.model.Type;
import com.cerberus.userservice.model.User;
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

import static org.mockito.Mockito.doNothing;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfirmationCodeTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final HttpHeaders headers = new HttpHeaders();

    @MockitoBean
    private MailService mailService;

    @BeforeAll
    public static void generateJwt(){
        JwtCreator jwtCreator = new JwtCreator();
        headers.setBearerAuth(jwtCreator.generateToken("ROLE_USER",  true));
    }

    @Test
    public void create(){
        HttpEntity<CreateConfirmationCodeRequest> httpEntity = new HttpEntity<>(new CreateConfirmationCodeRequest(Type.EMAIL, "rayan_thompson@gmail.com"), headers);

//        doNothing().when(mailService.sendEmail(new User(), 1_234_456))

        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/confirmation-codes/create",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("The email has been sent successfully.", responseEntity.getBody());
    }
}
