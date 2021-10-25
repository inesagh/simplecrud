package com.example.simplecrud.service.user;

import com.example.simplecrud.persistence.user.model.User;
import com.example.simplecrud.persistence.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository repository;


    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        User createdUser = repository.save(user);
        return createdUser;
    }

    @Override
    public User getById(Long id) {
        Optional<User> byId = repository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User update(Long id, User user) {
        Optional<User> byId = repository.findById(id);
        if(byId.isPresent()){
            byId.get().setFirstName(user.getFirstName());
            byId.get().setLastName(user.getLastName());
            byId.get().setUsername(user.getUsername());
            byId.get().setPassword(user.getPassword());
            return repository.save(byId.get());
        }else{
            return byId.orElse(null);
        }
    }

    @Override
    public boolean delete(Long id) {
        repository.delete(repository.getById(id));
        return !repository.existsById(id);
    }
}
