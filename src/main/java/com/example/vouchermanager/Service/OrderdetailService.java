package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderdetailService {
    List<OrderDetailDTO> findAll();
}
