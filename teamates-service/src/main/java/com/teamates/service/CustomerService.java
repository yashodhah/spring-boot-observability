package com.teamates.service;

import com.teamates.model.Customer;
import com.teamates.model.CustomerProfile;
import com.teamates.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// FLAW: all @Autowired field injection
// FLAW: no @Transactional
// FLAW: password stored in plain text
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // FLAW: no password hashing
    // FLAW: no duplicate email check before save
    public Customer registerCustomer(Customer customer) {
        // FLAW: UUID generation for customerId is not meaningful or ordered
        customer.setCustomerId(UUID.randomUUID().toString());
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        // FLAW: throws generic RuntimeException instead of custom domain exception
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    // FLAW: buildProfile loads data from Customer but doesn't synchronise changes
    public CustomerProfile buildProfile(Long customerId) {
        Customer customer = getCustomerById(customerId);
        CustomerProfile profile = new CustomerProfile();
        // FLAW: manual field-by-field mapping instead of using a mapper
        profile.setId(customer.getId());
        profile.setCustomerId(customer.getCustomerId());
        profile.setFirstName(customer.getFirstName());
        profile.setLastName(customer.getLastName());
        profile.setEmail(customer.getEmail());
        profile.setPhone(customer.getPhone());
        profile.setAddress(customer.getAddress());
        // FLAW: totalOrders and totalSpent are hardcoded to 0 – never populated
        profile.setTotalOrders(0);
        profile.setTotalSpent(0.0);
        profile.setMembershipLevel("BRONZE");  // FLAW: magic string
        return profile;
    }

    public List<Customer> getAllCustomers() {
        // FLAW: no pagination – loads all customers in memory
        return customerRepository.findAll();
    }

    // FLAW: updating customer exposes all fields including password
    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = getCustomerById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setPassword(updated.getPassword());  // FLAW: plain text password update
        return customerRepository.save(existing);
    }
}
