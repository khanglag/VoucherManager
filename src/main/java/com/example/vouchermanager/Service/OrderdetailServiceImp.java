package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import com.example.vouchermanager.Model.Entity.Orderdetail;
import com.example.vouchermanager.Repository.OrderdetailRepository;
import jakarta.transaction.Transactional;
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

    @Override
    public OrderDetailDTO findById(int id) {
        return orderdetailRepository.findById(id)
                .map(orderdetail -> new OrderDetailDTO(
                        orderdetail.getId(),
                        orderdetail.getOrderID().getId(),
                        orderdetail.getProductID().getId(),
                        orderdetail.getQuantity(),
                        orderdetail.getUnitPrice(),
                        orderdetail.getTotalPrice()
                )).orElse(null);
    }

    @Override
    public List<OrderDetailDTO> findByOrderId(int id) {
        return orderdetailRepository.findByOrderID_Id(id).stream()
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

    @Override
    public List<OrderDetailDTO> findByProductId(int id) {
        return orderdetailRepository.findByProductID_Id(id).stream()
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

    @Transactional
    public Orderdetail createOrderdetail(Orderdetail orderDetail) {
        return orderdetailRepository.save(orderDetail);
    }

    @Transactional
    public Orderdetail updateOrderdetail(int id, Orderdetail orderDetail) {
        Orderdetail orderdetailExist = orderdetailRepository.findById(id).orElse(null);
        if (orderdetailExist == null) {
            return null;
        }
        if (orderDetail.getOrderID() != null) {
            orderdetailExist.setOrderID(orderDetail.getOrderID());
        }
        if (orderDetail.getProductID() != null) {
            orderdetailExist.setProductID(orderDetail.getProductID());
        }
        if (orderDetail.getQuantity() != null) {
            orderdetailExist.setQuantity(orderDetail.getQuantity());
        }
        if (orderDetail.getUnitPrice() != null) {
            orderdetailExist.setUnitPrice(orderDetail.getUnitPrice());
        }
        if (orderDetail.getTotalPrice() != null) {
            orderdetailExist.setTotalPrice(orderDetail.getTotalPrice());
        }

        return orderdetailRepository.save(orderdetailExist);
    }
}
