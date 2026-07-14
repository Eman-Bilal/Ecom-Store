package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.User;
import com.example.EcomStore.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserService {
  UserRepository user;

  public List<User> getUser() {
    return user.findAll();
  }

  public ResponseEntity<String> createUser(){

    return ResponseEntity.ok("New user added");
  }

}
