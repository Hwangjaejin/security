package com.example.security.repository;

import com.example.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository가 없어도 IoC가 된다. JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User, Integer> {
}
