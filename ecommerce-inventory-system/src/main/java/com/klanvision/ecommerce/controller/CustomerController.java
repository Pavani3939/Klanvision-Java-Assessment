package com.klanvision.ecommerce.controller;

import com.klanvision.ecommerce.model.Customer;
import com.klanvision.ecommerce.service.EcommerceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final EcommerceService ecommerceService;

    public CustomerController(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody Customer customer) {
        Customer registered = ecommerceService.registerCustomer(customer);
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(ecommerceService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(ecommerceService.getCustomerById(id));
    }
}
