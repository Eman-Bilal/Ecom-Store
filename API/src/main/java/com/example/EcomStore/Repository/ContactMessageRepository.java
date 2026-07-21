package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

}