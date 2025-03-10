package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OrderdetailServiceImpTest {
    @Autowired
    OrderdetailServiceImp orderdetailServiceImp;

    @Test
    public void testFindAll(){
        List<OrderDetailDTO> orderDetailDTOS = orderdetailServiceImp.findAll();
        orderDetailDTOS.forEach(System.out::println);
    }
}
