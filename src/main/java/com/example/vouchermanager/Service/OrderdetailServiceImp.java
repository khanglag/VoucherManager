package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import com.example.vouchermanager.Repository.OrderdetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderdetailServiceImp implements OrderdetailService {

    @Autowired
    private OrderdetailRepository orderdetailRepository;

    @Override
    public List<OrderDetailDTO> findAll() {
        return orderdetailRepository.findAll().stream()
                .map(orderdetail -> new OrderDetailDTO(
                        orderdetail.getId(),
                        orderdetail.getOrderID().getId(),
                        orderdetail.getProductID().getId(),
                        orderdetail.getQuantity(),
                        orderdetail.getUnitPrice(),
                        orderdetail.getTotalPrice()
                ))
                .collect(Collectors.toList());
    }
}
