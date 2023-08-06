package com.assignment.customer.service;

import com.assignment.customer.model.CustomerModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {


    public List<CustomerModel> getAllCustomers();

    ResponseEntity<String> createCustomer(CustomerModel customer) throws JsonProcessingException;

    ResponseEntity<String> updateCustomer(String uuid, CustomerModel customer) throws JsonProcessingException;

    ResponseEntity<String> deleteCustomer(String uuid);
    ResponseEntity<String> loginUser(String loginId, String password) throws JsonProcessingException;
}
