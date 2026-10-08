package com.klanvision.ecommerce.service;

import com.klanvision.ecommerce.dto.CartItemRequest;
import com.klanvision.ecommerce.dto.OrderPlacementRequest;
import com.klanvision.ecommerce.model.*;
import com.klanvision.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EcommerceService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderRepository customerOrderRepository;

    public EcommerceService(CustomerRepository customerRepository,
                            ProductRepository productRepository,
                            CustomerOrderRepository customerOrderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.customerOrderRepository = customerOrderRepository;
    }

    // --- Customer Management ---
    @Transactional
    public Customer registerCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + customer.getEmail() + " already exists.");
        }
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
    }

    // --- Product Management ---
    @Transactional
    public Product addProduct(Product product) {
        if (productRepository.existsByProductCode(product.getProductCode())) {
            throw new IllegalArgumentException("Product code " + product.getProductCode() + " already exists.");
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.searchProducts(query.trim());
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    @Transactional
    public Product updateStock(Long productId, Integer additionalQuantity) {
        Product product = getProductById(productId);
        product.setAvailableQuantity(product.getAvailableQuantity() + additionalQuantity);
        return productRepository.save(product);
    }

    // --- Order & Inventory Management ---
    @Transactional
    public CustomerOrder placeOrder(OrderPlacementRequest request) {
        Customer customer = getCustomerById(request.getCustomerId());
        CustomerOrder order = new CustomerOrder(customer);
        double grandTotal = 0.0;

        for (CartItemRequest itemReq : request.getItems()) {
            Product product = getProductById(itemReq.getProductId());

            if (product.getAvailableQuantity() < itemReq.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getAvailableQuantity() + ", Requested: " + itemReq.getQuantity());
            }

            // Deduct inventory
            product.setAvailableQuantity(product.getAvailableQuantity() - itemReq.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(product, itemReq.getQuantity());
            order.addItem(orderItem);
            grandTotal += orderItem.getTotalPrice();
        }

        order.setTotalAmount(grandTotal);
        return customerOrderRepository.save(order);
    }

    public List<CustomerOrder> getAllOrders() {
        return customerOrderRepository.findAll();
    }

    public List<CustomerOrder> getOrdersByCustomer(Long customerId) {
        getCustomerById(customerId);
        return customerOrderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public CustomerOrder getOrderById(Long id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }

    @Transactional
    public CustomerOrder updateOrderStatus(Long orderId, OrderStatus newStatus) {
        CustomerOrder order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled orders cannot change status.");
        }

        if (newStatus == OrderStatus.CANCELLED) {
            // Automatically RESTOCK product stock when order is cancelled
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setAvailableQuantity(product.getAvailableQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(newStatus);
        return customerOrderRepository.save(order);
    }
}
