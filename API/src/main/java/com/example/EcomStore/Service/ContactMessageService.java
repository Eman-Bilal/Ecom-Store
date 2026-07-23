package com.example.EcomStore.Service;

import com.example.EcomStore.Dto.ContactRequest;
import com.example.EcomStore.Entities.ContactMessage;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContactMessageService {

  private final ContactMessageRepository contactMessageRepository;
  private final EmailService emailService;

  public ContactMessage submit(ContactRequest request) {
    ContactMessage message = new ContactMessage();
    message.setName(request.getName());
    message.setEmail(request.getEmail());
    message.setSubject(request.getSubject());
    message.setMessage(request.getMessage());
    emailService.sendAdminNotification("Contact us form received",
        "A new contact us form received with Subject: "+ request.getSubject());
    return contactMessageRepository.save(message);
  }

  public List<ContactMessage> getAll() {
    return contactMessageRepository.findAll();
  }

  public ContactMessage getById(Long id) {
    return contactMessageRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));
  }
  public ContactMessage markResolved(Long id) {
    ContactMessage message = getById(id);
    message.setResolved(true);
    return contactMessageRepository.save(message);
  }
}