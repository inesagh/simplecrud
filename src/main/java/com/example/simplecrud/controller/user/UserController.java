package com.example.simplecrud.controller.user;

import com.example.simplecrud.persistence.exceptionhandlers.RecordNotFoundException;
import com.example.simplecrud.persistence.user.model.User;
import com.example.simplecrud.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Validated
public class UserController {
    private final UserServiceImpl service;

    @Autowired
    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping("users")
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {
        User createdUser = service.create(user);
        if (createdUser != null) {
            return ResponseEntity.status(201).body(createdUser);
        }
        return ResponseEntity.status(400).body("Provided user hasn't been created.");
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

//    @GetMapping(value = "usersXML", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<List<User>> getAllXML() {
//        return ResponseEntity.ok(service.getAll());
//    }

    @GetMapping(value = "users/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        User byId = service.getById(id);
        if (byId != null) {
            return ResponseEntity.ok(byId);
        } else {
            throw new RecordNotFoundException("Invalid user id : " + id);
        }
    }

    @PutMapping("users/{id}")
    public ResponseEntity<Object> edit(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = service.update(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        throw new RecordNotFoundException("Invalid user id : " + id);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        if (service.getById(id) != null) {
            return ResponseEntity.ok(service.delete(id));
        } else {
            return ResponseEntity.ok(false);
        }

    }

    private User findById(Long id) {
        ArrayList<User> userList = new ArrayList<>();
        List<User> list = userList.stream().filter(each -> each.getId() == id).collect(Collectors.toList());
        if (list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
