package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.User;
import com.example.EcomStore.Repository.UserRepository;
import com.example.EcomStore.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user/")
public class UserController {

  UserService user;

  @GetMapping("getAll")
  public List<User> getAll(){
    return user.getUser();
  }


}
