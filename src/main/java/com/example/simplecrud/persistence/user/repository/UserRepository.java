package com.example.simplecrud.persistence.user.repository;

import com.example.simplecrud.persistence.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
