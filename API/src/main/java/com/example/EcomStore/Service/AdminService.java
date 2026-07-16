package com.example.EcomStore.Service;

import com.example.EcomStore.Dto.RegisterRequest;
import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Entities.Role;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {

  private final AdminRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Admin register(RegisterRequest user) {
    Admin admin= new Admin();
    admin.setFirstName(user.getFirstName());
    admin.setLastName(user.getLastName());
    admin.setEmail(user.getEmail());
    admin.setPassword(passwordEncoder.encode(user.getPassword()));
    admin.setPhone(user.getPhone());
    admin.setRole((Role.ADMIN));

    return userRepository.save(admin);
  }

  public Admin findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
  }

  public List<Admin> getAll(){
      return userRepository.findAll();
  }
}