package com.example.springboot.repositores;

import com.example.springboot.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID>{

    UserModel findByName(String name);
}