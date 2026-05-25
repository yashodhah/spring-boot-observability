package com.teamates.controller;

import com.teamates.model.Store;
import com.teamates.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLAW: controller directly accesses repository without any service layer
// FLAW: no error handling, no validation
// FLAW: @Autowired field injection
@RestController
@RequestMapping("api/v1/stores")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;  // FLAW: no service layer

    @PostMapping
    public ResponseEntity<Store> createStore(@RequestBody Store store) {
        // FLAW: no validation, entity used directly as request body
        return ResponseEntity.ok(storeRepository.save(store));
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStore(@PathVariable int id) {
        // FLAW: orElse(null) instead of orElseThrow or returning 404
        return ResponseEntity.ok(storeRepository.findById(id).orElse(null));
    }

    // FLAW: DELETE /stores/{id} with no cascading logic or safety checks
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable int id) {
        storeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // FLAW: PUT replaces by saving new entity rather than updating
    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable int id, @RequestBody Store store) {
        store.setId(id);  // FLAW: bypassing ORM – no fetch-then-update pattern
        return ResponseEntity.ok(storeRepository.save(store));
    }
}
