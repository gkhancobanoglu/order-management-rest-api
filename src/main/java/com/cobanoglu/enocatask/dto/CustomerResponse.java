package com.cobanoglu.enocatask.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String address;
}
