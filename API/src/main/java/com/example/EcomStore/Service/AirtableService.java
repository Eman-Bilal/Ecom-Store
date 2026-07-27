package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.CustomerOrder;
import com.example.EcomStore.Entities.OrderItems;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AirtableService {

  private final RestTemplate restTemplate = new RestTemplate(new JdkClientHttpRequestFactory());

  @Value("${airtable.api.token}")
  private String apiToken;

  @Value("${airtable.base.id}")
  private String baseId;

  @Value("${airtable.table.name}")
  private String tableName;

  @Async
  public void syncOrder(CustomerOrder order, List<OrderItems> items) {
    try {
      String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

      String itemsSummary = (items == null || items.isEmpty()) ? "" :
          items.stream()
              .map(i -> i.getProductName() + " x" + i.getQuantity())
              .collect(Collectors.joining(", "));

      Map<String, Object> record = getRecord(order, itemsSummary);

      Map<String, Object> performUpsert = new HashMap<>();
      performUpsert.put("fieldsToMergeOn", List.of("Order Number"));

      Map<String, Object> body = new HashMap<>();
      body.put("performUpsert", performUpsert);
      body.put("records", List.of(record));

      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(apiToken);
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
      restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);

    } catch (Exception e) {
      log.error("Failed to sync order {} to Airtable: {}", order.getOrderNumber(), e.getMessage());
    }
  }

  private static @NonNull Map<String, Object> getRecord(CustomerOrder order, String itemsSummary) {
    Map<String, Object> fields = new HashMap<>();
    fields.put("Order Number", order.getOrderNumber());
    fields.put("Customer Name", order.getFirstName() + " " + order.getLastName());
    fields.put("Email", order.getEmail());
    fields.put("Total Amount", order.getTotalAmount());
    fields.put("Status", order.getOrderStatus().name());
    fields.put("Placed At", order.getCreatedAt() != null ? order.getCreatedAt().toString() : "");
    fields.put("Items", itemsSummary);

    Map<String, Object> record = new HashMap<>();
    record.put("fields", fields);
    return record;
  }

}