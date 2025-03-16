package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.DTO.UserDTO;
import com.example.vouchermanager.Model.Entity.Order;
import com.example.vouchermanager.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUserID() != null ? order.getUserID().getId() : 0, // Kiểm tra null tránh lỗi
                        order.getOrderDate() != null ? order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null,
                        order.getTotalAmount(),
                        order.getFinalAmount(),
                        order.getOrderStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findById(int id) {
        return orderRepository.findById(id)
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUserID().getId(),
                        order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                        order.getTotalAmount(),
                        order.getFinalAmount(),
                        order.getOrderStatus()
                ))
                .orElse(null);
    }

    @Transactional
    @Override
    public List<OrderDTO> findByUserId(int userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUserID().getId(),
                        order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                        order.getTotalAmount(),
                        order.getFinalAmount(),
                        order.getOrderStatus()
                ))
                .collect(Collectors.toList());
    }




}
