package com.klanvision.ecommerce;

import com.klanvision.ecommerce.dto.CartItemRequest;
import com.klanvision.ecommerce.dto.OrderPlacementRequest;
import com.klanvision.ecommerce.model.Customer;
import com.klanvision.ecommerce.model.CustomerOrder;
import com.klanvision.ecommerce.model.OrderStatus;
import com.klanvision.ecommerce.model.Product;
import com.klanvision.ecommerce.service.EcommerceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EcommerceServiceTest {

    @Autowired
    private EcommerceService ecommerceService;

    @Test
    public void testOrderPlacementAndStockManagement() {
        Customer c = ecommerceService.registerCustomer(new Customer(null, "Bob Smith", "bob@example.com", "1112223333", "123 Main St"));
        Product p = ecommerceService.addProduct(new Product(null, "TEST-PRD-01", "Test Keyboard", "Peripherals", 1500.0, 10));

        OrderPlacementRequest req = new OrderPlacementRequest();
        req.setCustomerId(c.getId());
        req.setItems(List.of(new CartItemRequest(p.getId(), 3)));

        CustomerOrder order = ecommerceService.placeOrder(req);
        assertNotNull(order.getId());
        assertEquals(4500.0, order.getTotalAmount());

        // Check stock reduced to 7
        Product updatedProduct = ecommerceService.getProductById(p.getId());
        assertEquals(7, updatedProduct.getAvailableQuantity());

        // Cancel order and verify stock restocked back to 10
        ecommerceService.updateOrderStatus(order.getId(), OrderStatus.CANCELLED);
        Product restockedProduct = ecommerceService.getProductById(p.getId());
        assertEquals(10, restockedProduct.getAvailableQuantity());
    }

    @Test
    public void testOutOfStockPrevention() {
        Customer c = ecommerceService.registerCustomer(new Customer(null, "Charlie", "charlie@example.com", "9990001111", "456 Side St"));
        Product p = ecommerceService.addProduct(new Product(null, "TEST-PRD-02", "Limited Item", "Gadgets", 500.0, 2));

        OrderPlacementRequest req = new OrderPlacementRequest();
        req.setCustomerId(c.getId());
        req.setItems(List.of(new CartItemRequest(p.getId(), 5))); // Requesting 5 when only 2 available

        assertThrows(IllegalStateException.class, () -> ecommerceService.placeOrder(req));
    }
}
