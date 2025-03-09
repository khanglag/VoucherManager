package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OrderTest {
    @Autowired
    OrderServiceImp orderService;

    @Test
    public void test() {
        List<OrderDTO> orders = orderService.findAll();
        orders.forEach(order -> System.out.println(order));
    }
}
