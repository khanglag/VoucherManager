package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.Entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public interface OrderService {
    Page<OrderDTO> findAll(Pageable pageable);
    OrderDTO findById(int id);
    Page<OrderDTO> findByUserId(int userId, Pageable pageable);
    Page<OrderDTO> getOrdersByDateRange(Instant startDate, Instant endDate, Pageable pageable);
    Page<OrderDTO> findByStatus(String status, Pageable pageable);
    BigDecimal getTotalFinalAmountForMonth(int month, int year);
    List<OrderDTO> findAllByUserId(int userId);
}
