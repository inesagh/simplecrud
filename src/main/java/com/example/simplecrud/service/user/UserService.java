package com.example.simplecrud.service.user;

import com.example.simplecrud.persistence.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);
    User getById(Long id);
    List<User> getAll();
    User update(Long id, User user);
    boolean delete(Long id);
}
