package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.Entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public interface OrderService {
    List<OrderDTO> findAll();
    OrderDTO findById(int id);
    List<OrderDTO> findByUserId(int userId);
    List<OrderDTO> getOrdersByDateRange(Instant startDate, Instant endDate);
    List<OrderDTO> findByStatus(String status);

}
