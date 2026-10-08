package com.klanvision.ecommerce.controller;

import com.klanvision.ecommerce.dto.OrderPlacementRequest;
import com.klanvision.ecommerce.model.CustomerOrder;
import com.klanvision.ecommerce.model.OrderStatus;
import com.klanvision.ecommerce.service.EcommerceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final EcommerceService ecommerceService;

    public OrderController(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @PostMapping("/place")
    public ResponseEntity<CustomerOrder> placeOrder(@Valid @RequestBody OrderPlacementRequest request) {
        CustomerOrder order = ecommerceService.placeOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerOrder>> getAllOrders() {
        return ResponseEntity.ok(ecommerceService.getAllOrders());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerOrder>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(ecommerceService.getOrdersByCustomer(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ecommerceService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CustomerOrder> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(ecommerceService.updateOrderStatus(id, status));
    }
}
