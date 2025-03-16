package com.cobanoglu.enocatask.mapper;

import com.cobanoglu.enocatask.dto.CustomerRequest;
import com.cobanoglu.enocatask.dto.CustomerResponse;
import com.cobanoglu.enocatask.model.Customer;

public class CustomerMapper {

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }

    public static Customer toCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setSurname(request.getSurname());
        customer.setEmail(request.getEmail());
        customer.setPassword(request.getPassword());
        customer.setAddress(request.getAddress());
        return customer;
    }
}
