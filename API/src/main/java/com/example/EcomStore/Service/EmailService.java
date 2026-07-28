package com.example.EcomStore.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.aspectj.apache.bcel.classfile.ClassParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final VelocityEngine velocityEngine;

  @Value("${admin.notification.email}")
  private String adminEmail;

//  @Async
//  public void sendAdminNotification(String subject, String body) {
//    sendEmail(adminEmail, subject, body);
//  }

  @Async
  public void sendEmail(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);
    mailSender.send(message);
  }

  private String renderTemplate(String templateName, Map<String, Object> variables){
    VelocityContext context= new VelocityContext(variables);
    StringWriter writer = new StringWriter();
    velocityEngine.getTemplate("templates/"+templateName).merge(context,writer);
    return  writer.toString();
  }

  private void sendHtmlWithImage(String to, String subject, String htmlBody){
    try{
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlBody,true);

      ClassPathResource logo= new ClassPathResource("static/images/logo.jpg");
      helper.addInline("logoImage", logo);

      mailSender.send(message);

    }catch (Exception e){
      log.error("Failed to send email from sendHtmlWithImage() to {} : {}", to, e.getMessage());
    }
  }

  @Async
  public void sendOrderConfirmation(@Email(message = "Invalid email") @NotBlank(message = "Email is required") String email,
                                    @NotBlank(message = "First name is required") String firstName,
                                    String lastName, String status,
                                    String orderNumber, @DecimalMin(value = "0.0") BigDecimal totalAmount){
    Map<String, Object> vars = new HashMap<>();
    vars.put("firstName", firstName);
    vars.put("lastName", lastName);
    vars.put("orderNumber", orderNumber);
    vars.put("status", status);
    vars.put("totalAmount", totalAmount);

    String html = renderTemplate("order-confirmation.vm", vars);
    sendHtmlWithImage(email, "Order Confirmation: " + orderNumber , html);
  }

  private void send(String to, String subject, String body) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, false);
      mailSender.send(message);
    } catch (Exception e) {
      log.error("Failed to send email to {}: {}", to, e.getMessage());
    }
  }

  @Async
  public void sendAdminNotification(String subject, String body) {
    Map<String, Object> vars = new HashMap<>();
    vars.put("subject", subject);
    vars.put("body", body);
    String html = renderTemplate("admin-notification.vm", vars);
    sendHtmlWithImage(adminEmail, subject, html);
  }

  @Async
  public void sendOrderStatusUpdate(String to, String firstName, String orderNumber, String status) {
    Map<String, Object> vars = new HashMap<>();
    vars.put("firstName", firstName);
    vars.put("orderNumber", orderNumber);
    vars.put("status", status);
    send(to, "Order " + orderNumber + " Update", renderTemplate("order-status-update.vm", vars));
  }

  @Async
  public void sendOrderCancelled(String to, String firstName, String orderNumber) {
    Map<String, Object> vars = new HashMap<>();
    vars.put("firstName", firstName);
    vars.put("orderNumber", orderNumber);
    sendHtmlWithImage(to, "Order " + orderNumber + " Cancelled", renderTemplate("order-cancelled.vm", vars));
  }
}