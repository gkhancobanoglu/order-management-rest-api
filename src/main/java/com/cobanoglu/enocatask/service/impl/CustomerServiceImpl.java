package com.cobanoglu.enocatask.service.impl;

import com.cobanoglu.enocatask.dto.CustomerRequest;
import com.cobanoglu.enocatask.dto.CustomerResponse;
import com.cobanoglu.enocatask.exception.CustomerNotFoundException;
import com.cobanoglu.enocatask.mapper.CustomerMapper;
import com.cobanoglu.enocatask.model.Customer;
import com.cobanoglu.enocatask.repository.CustomerRepository;
import com.cobanoglu.enocatask.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponse addCustomer(CustomerRequest request) {
        Customer customer = CustomerMapper.toCustomer(request);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return CustomerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        existingCustomer.setName(request.getName());
        existingCustomer.setSurname(request.getSurname());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setAddress(request.getAddress());

        if (!existingCustomer.getPassword().equals(request.getPassword())) {
            existingCustomer.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return CustomerMapper.toCustomerResponse(customerRepository.save(existingCustomer));
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return CustomerMapper.toCustomerResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
