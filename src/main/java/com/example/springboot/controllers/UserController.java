package com.example.springboot.controllers;

import com.example.springboot.dtos.UserRecordDto;
import com.example.springboot.models.UserModel;
import com.example.springboot.repositores.IUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    IUserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<UserModel> saveuser(@RequestBody @Valid UserRecordDto userRecordDto) {

        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(userRecordDto, userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> login(@RequestBody @Valid UserRecordDto userRecordDto) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        Optional<UserModel> userO = Optional.ofNullable(userRepository.findByName(userModel.getName()));
        if (userO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("user not found.");
        } else if (!userO.get().getPassword().equals(userModel.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body("password wrong.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userO.get());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllusers() {
        List<UserModel> usersList = userRepository.findAll();
        if (!usersList.isEmpty()) {
            for (UserModel user : usersList) {
                UUID id = user.getId();
                user.add(linkTo(methodOn(UserController.class).getOneuser(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getOneuser(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found.");
        }
        userO.get().add(linkTo(methodOn(UserController.class).getAllusers()).withRel("users List"));
        return ResponseEntity.status(HttpStatus.OK).body(userO.get());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateuser(@PathVariable(value = "id") UUID id,
            @RequestBody @Valid UserRecordDto userRecordDto) {
        Optional<UserModel> userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found.");
        }
        var userModel = userO.get();
        BeanUtils.copyProperties(userRecordDto, userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteuser(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found.");
        }
        userRepository.delete(userO.get());
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }
}
