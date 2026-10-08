package com.klanvision.ecommerce.config;

import com.klanvision.ecommerce.dto.CartItemRequest;
import com.klanvision.ecommerce.dto.OrderPlacementRequest;
import com.klanvision.ecommerce.model.Customer;
import com.klanvision.ecommerce.model.Product;
import com.klanvision.ecommerce.service.EcommerceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initEcommerceData(EcommerceService ecommerceService) {
        return args -> {
            // Pre-load Customers
            Customer cust1 = ecommerceService.registerCustomer(new Customer(null, "Pavani Yellaturu", "pavani.ecom@example.com", "+91 9876543210", "Flat 402, Greenfield Heights, Hyderabad"));
            Customer cust2 = ecommerceService.registerCustomer(new Customer(null, "Vikram Reddy", "vikram@example.com", "+91 9812345678", "Plot 12, Jubilee Hills, Hyderabad"));

            // Pre-load Products
            Product p1 = ecommerceService.addProduct(new Product(null, "PRD-LAP-001", "UltraBook Pro 15", "Electronics", 75000.0, 15));
            Product p2 = ecommerceService.addProduct(new Product(null, "PRD-PHN-002", "SmartPhone X20", "Electronics", 42000.0, 30));
            Product p3 = ecommerceService.addProduct(new Product(null, "PRD-AUD-003", "Noise Cancelling Headphones", "Accessories", 6500.0, 50));
            Product p4 = ecommerceService.addProduct(new Product(null, "PRD-WCH-004", "Fitness Smartwatch v2", "Wearables", 4999.0, 25));

            // Pre-load Sample Order
            OrderPlacementRequest req = new OrderPlacementRequest();
            req.setCustomerId(cust1.getId());
            req.setItems(List.of(
                new CartItemRequest(p1.getId(), 1),
                new CartItemRequest(p3.getId(), 2)
            ));
            ecommerceService.placeOrder(req);
        };
    }
}
