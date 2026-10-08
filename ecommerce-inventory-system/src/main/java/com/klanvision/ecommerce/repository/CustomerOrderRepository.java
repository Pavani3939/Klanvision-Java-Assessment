package com.klanvision.ecommerce.repository;

import com.klanvision.ecommerce.model.CustomerOrder;
import com.klanvision.ecommerce.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<CustomerOrder> findByStatusOrderByCreatedAtDesc(OrderStatus status);
}
