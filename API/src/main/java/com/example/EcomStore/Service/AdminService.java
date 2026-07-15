package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.Admin;
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

  public Admin register(Admin user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public Admin findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
  }

  public List<Admin> getAll(){
      return userRepository.findAll();
  }
}