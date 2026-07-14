package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Service.AdminService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user/")

public class AdminController {

  AdminService user;

  @GetMapping("getAll")
  public List<Admin> getAll(){
    return user.getUser();
  }

  @PostMapping("create")
  public ResponseEntity<String> create(){

    return new ResponseEntity<>(HttpStatusCode.valueOf(200));
  }


}
