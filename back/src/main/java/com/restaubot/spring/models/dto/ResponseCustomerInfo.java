package com.restaubot.spring.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCustomerInfo {
    private Integer customerId;
    private String mail;
    private String role;
}
