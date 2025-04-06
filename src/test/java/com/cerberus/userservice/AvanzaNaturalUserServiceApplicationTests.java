package com.cerberus.userservice;

import com.cerberus.userservice.model.CodeVerificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvanzaNaturalUserServiceApplicationTests {

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    private final HttpHeaders headers = new HttpHeaders();


}
