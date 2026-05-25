package com.teamates.controller;

import com.teamates.model.Customer;
import com.teamates.model.CustomerProfile;
import com.teamates.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLAW: @Autowired field injection
// FLAW: password exposed in all customer responses
// FLAW: no authentication/authorization guards
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // FLAW: no @Valid, full Customer entity (including password) sent in request body
    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody Customer customer) {
        // FLAW: returns saved customer including plain text password
        return ResponseEntity.ok(customerService.registerCustomer(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        // FLAW: returns full Customer entity with password field
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<CustomerProfile> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.buildProfile(id));
    }

    // FLAW: returns all customers with no pagination or access control
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // FLAW: PUT /customers/{id} but body contains full entity including password
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                   @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }
}
