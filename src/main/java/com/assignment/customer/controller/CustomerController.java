package com.assignment.customer.controller;


import com.assignment.customer.model.CustomerModel;
import com.assignment.customer.service.CustomerService;
import com.assignment.customer.service.CustomerServiceIMPL;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService service;
    private String bearerToken;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginData) throws JsonProcessingException {
        String loginId = loginData.get("login_id");
        String password = loginData.get("password");

        return service.loginUser(loginId, password);

    }


    @PostMapping("/deleteCustomer")
    public ResponseEntity<String> deleteCustomer(@RequestParam("uuid") String uuid) {
        return service.deleteCustomer(uuid);
    }

    @PostMapping("/updateCustomer/{uuid}")
    public ResponseEntity<String> updateCustomer(@PathVariable String uuid, @RequestBody CustomerModel customer) throws JsonProcessingException {
        return service.updateCustomer(uuid, customer);
    }

    @GetMapping("/getAllCustomers")
    public List<CustomerModel> getAllCustomers(){
        return service.getAllCustomers();
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerModel customer) throws JsonProcessingException {
        return service.createCustomer(customer);
    }



    }
