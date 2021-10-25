package com.example.simplecrud.service;

import com.example.simplecrud.persistence.user.model.User;
import com.example.simplecrud.persistence.user.repository.UserRepository;
import com.example.simplecrud.service.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl  userService;
    @Mock
    private UserRepository userRepository;

//    @BeforeEach
//    private void setUp(){
//        userService = new UserServiceImpl(userRepository);
//    }

    @Test
    @DisplayName("Test for creating user.")
    public void testCreate(){
        User user = new User(1L, "asd", "asdyan");
        User createdUser = new User(1L, "asd", "asdyan");
        Mockito.when(userRepository.save(user)).thenReturn(createdUser);
        userService.create(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    @DisplayName("Test for getting the user by id.")
    public void testGetById(){
        User user = new User(5L, "kkk", "hhhh");
        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        userService.getById(5L);
        Mockito.verify(userRepository, Mockito.times(1)).findById(5L);
    }

    @Test
    @DisplayName("Test for getting all users.")
    public void testGetAll(){
        List<User> userList = new ArrayList<>();
        userList.add(new User(5L, "kkk", "hhhh"));
        userList.add(new User(8L, "aaaaaaaaaa", "aaaaaaaaaaaaaa"));
        userList.add(new User(9L, "aaaaaa", "AAAAAAAAAAAAAAAA"));
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        userService.getAll();

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Test for updating the user by id.")
    public void testUpdate(){
        User user = new User(5L, "kkk", "HHHH");
        Optional<User> gotUser = Optional.of(new User(5L, "kkk", "hhhh"));
        Mockito.when(userRepository.findById(5L)).thenReturn(gotUser);
        gotUser.get().setFirstName(user.getFirstName());
        gotUser.get().setLastName(user.getLastName());
        Mockito.when(userRepository.save(gotUser.get())).thenReturn(user);

        userService.update(5L, user);
        Mockito.verify(userRepository, Mockito.times(1)).findById(5L);
        Mockito.verify(userRepository, Mockito.times(1)).save(gotUser.get());
    }

    @Test
    @DisplayName("Test for deleting the user by id.")
    public void testDelete(){
        User user = new User(5L, "asd", "hhhh");
        Mockito.when(userRepository.getById(5L)).thenReturn(user);
        userService.delete(5L);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
        Assertions.assertFalse(userRepository.existsById(5L));
    }
}
