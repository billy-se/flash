package com.backend.engine.repository;

import com.backend.engine.model.FlashSaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<FlashSaleOrder, Long> {
    List<FlashSaleOrder> findByUserId(String userId);
}