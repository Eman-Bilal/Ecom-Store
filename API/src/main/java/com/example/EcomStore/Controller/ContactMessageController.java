package com.example.EcomStore.Controller;

import com.example.EcomStore.Dto.ContactRequest;
import com.example.EcomStore.Entities.ContactMessage;
import com.example.EcomStore.Service.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactMessageController {

  private final ContactMessageService contactMessageService;

  @PostMapping
  public ResponseEntity<ContactMessage> submit(@Valid @RequestBody ContactRequest request) {
    ContactMessage saved = contactMessageService.submit(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<ContactMessage>> getAll() {
    return ResponseEntity.ok(contactMessageService.getAll());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<ContactMessage> getById(@PathVariable Long id) {
    return ResponseEntity.ok(contactMessageService.getById(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/resolve")
  public ResponseEntity<ContactMessage> markResolved(@PathVariable Long id) {
    return ResponseEntity.ok(contactMessageService.markResolved(id));
  }
}