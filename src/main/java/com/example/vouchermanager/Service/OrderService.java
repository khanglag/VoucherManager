package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    List<OrderDTO> findAll();
}
