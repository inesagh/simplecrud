package com.example.simplecrud.controller.user;

import com.example.simplecrud.persistence.user.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";
    private static TestRestTemplate restTemplate = null;

    @BeforeAll
    public static void init() {
        restTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/users");
    }

    @Test
    @DisplayName("Test for getting the user by id.")
    @Sql(statements = "INSERT INTO user(id, first_name, last_name, username, password) values(1, 'asd', 'asdyan', 'asd', 'asd')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetById() {
        User user = restTemplate.getForObject(baseUrl.concat("/{id}"), User.class, 1);
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals("asd", user.getFirstName()),
                () -> assertEquals("asdyan", user.getLastName()),
                () -> assertEquals("asd", user.getUsername()),
                () -> assertEquals("asd", user.getLastName())
        );
    }

    @Test
    @DisplayName("Test for getting all users.")
    @Sql(statements = "INSERT INTO user(id, first_name, last_name, username, password) values (1, 'ASD', 'ASDYAN', 'ASD', 'ASDYAN')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAll() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "ASD", "ASDYAN", "ASD", "ASD"));

        ResponseEntity<User[]> forEntity = restTemplate.getForEntity(baseUrl, User[].class);
        User[] userArray = forEntity.getBody();
        assert userArray != null;
        List<User> collect = Arrays.stream(userArray)
                .collect(Collectors.toList());
        assertEquals(userList.get(0).getFirstName(), collect.get(0).getFirstName());
        assertEquals(userList.get(0).getLastName(), collect.get(0).getLastName());
        assertEquals(userList.get(0).getUsername(), collect.get(0).getUsername());
        assertEquals(userList.get(0).getPassword(), collect.get(0).getPassword());
    }

    @Test
    @DisplayName("Test for creating an user.")
    @Sql(statements = "DELETE FROM user",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreate() {
        User user = restTemplate.postForEntity(baseUrl, new User(1L, "asd", "asdyan", "asd", "asdyan"), User.class).getBody();
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals("asd", user.getFirstName()),
                () -> assertEquals("asdyan", user.getLastName()),
                () -> assertEquals("asd", user.getUsername()),
                () -> assertEquals("asdyan", user.getPassword())
        );
    }

    @Test
    @DisplayName("Test for updating the user by id.")
    @Sql(statements = "INSERT INTO user(id, first_name, last_name) values(1, 'asd', 'asdyan')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdate() {
        User user = new User(1L, "Asd", "asdyan", "asd", "asd");
        HttpEntity<User> requestEntity = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = restTemplate.exchange(baseUrl.concat("/{id}"),
                HttpMethod.PUT,
                requestEntity,
                User.class,
                1);
        assertNotNull(responseEntity.getBody());
        assertEquals("Asd", responseEntity.getBody().getFirstName());
    }

    @Test
    @DisplayName("Test for deleting the user by id.")
    @Sql(statements = "INSERT INTO user(id, first_name, last_name, user_name, password) values(1, 'asd', 'asdyan', 'asd', 'asd')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDelete() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> deleted = restTemplate.exchange(baseUrl.concat("/{id}"), HttpMethod.DELETE, entity, String.class, 1);
        assertEquals(deleted.getStatusCode(), HttpStatus.OK);

        User[] body = restTemplate.getForEntity(baseUrl, User[].class).getBody();
        long count = Arrays.stream(body).filter(each -> each.getId() == 1).count();
        assertEquals(0, count);
    }
}

