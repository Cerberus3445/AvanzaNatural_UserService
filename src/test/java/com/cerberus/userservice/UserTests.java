package com.cerberus.userservice;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.model.Role;
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

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    public static void generateJwt(){
        JwtCreator jwtCreator = new JwtCreator();
        headers.setBearerAuth(jwtCreator.generateToken("ROLE_USER",  true));
    }

    @Test
    public void update(){
        UserDto userDto = new UserDto(null, "newFirstName", "lastName", "email@gmail.com", "password", false, Role.ROLE_USER);
        HttpEntity<UserDto> createEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/users/2",
                HttpMethod.PATCH,
                createEntity,
                String.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("User has been updated.", responseEntity.getBody());

        UserDto updatedUserDto = this.testRestTemplate.exchange(
                "/api/v1/users/2",
                HttpMethod.GET,
                createEntity,
                UserDto.class
        ).getBody();

        Assertions.assertEquals(updatedUserDto.getFirstName(), userDto.getFirstName());
    }


    @Test
    public void get(){
        ResponseEntity<UserDto> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/users/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserDto.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("Mike", responseEntity.getBody().getFirstName());
    }

    @Test
    public void delete(){
        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/users/3",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("User has been deleted.", responseEntity.getBody());
    }
}
