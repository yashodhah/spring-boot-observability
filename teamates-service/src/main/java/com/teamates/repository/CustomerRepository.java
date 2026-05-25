package com.teamates.repository;

import com.teamates.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    // FLAW: both customerId and id act as identifiers – confusing API
    Optional<Customer> findByCustomerId(String customerId);
}
