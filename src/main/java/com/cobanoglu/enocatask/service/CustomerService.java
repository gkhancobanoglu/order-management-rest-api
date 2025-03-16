package com.cobanoglu.enocatask.service;

import com.cobanoglu.enocatask.dto.CustomerRequest;
import com.cobanoglu.enocatask.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse addCustomer(CustomerRequest request);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    CustomerResponse getCustomer(Long id);

    List<CustomerResponse> getAllCustomers();

    void deleteCustomer(Long id);
}
