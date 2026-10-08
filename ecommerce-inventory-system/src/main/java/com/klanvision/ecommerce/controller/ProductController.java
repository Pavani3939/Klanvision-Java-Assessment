package com.klanvision.ecommerce.controller;

import com.klanvision.ecommerce.model.Product;
import com.klanvision.ecommerce.service.EcommerceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final EcommerceService ecommerceService;

    public ProductController(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product created = ecommerceService.addProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(ecommerceService.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(ecommerceService.searchProducts(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ecommerceService.getProductById(id));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestParam Integer additionalQuantity) {
        return ResponseEntity.ok(ecommerceService.updateStock(id, additionalQuantity));
    }
}
