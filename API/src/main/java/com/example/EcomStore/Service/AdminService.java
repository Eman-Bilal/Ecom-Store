package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Repository.AdminRepository;
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
public class AdminService {
  AdminRepository user;

  public List<Admin> getUser() {
    return user.findAll();
  }

  public ResponseEntity<String> createUser(){

    return ResponseEntity.ok("New user added");
  }

}
