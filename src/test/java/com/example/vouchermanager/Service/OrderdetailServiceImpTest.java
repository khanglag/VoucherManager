package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import com.example.vouchermanager.Model.Entity.Order;
import com.example.vouchermanager.Model.Entity.Orderdetail;
import com.example.vouchermanager.Model.Entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class OrderdetailServiceImpTest {
    @Autowired
    OrderdetailServiceImp orderdetailServiceImp;

    @Test
    public void testFindAll(){
//        List<OrderDetailDTO> orderDetailDTOS = orderdetailServiceImp.findAll();
//        orderDetailDTOS.forEach(System.out::println);
        int page = 0;
        int size = 10;

    }
    @Test
    public void testFindById(){
        OrderDetailDTO orderDetailDTO = orderdetailServiceImp.findById(1);
        System.out.println(orderDetailDTO);
    }
    @Test
    public void testFindByOrderId(){
//        List<OrderDetailDTO> orderDetailDTOS = orderdetailServiceImp.findByOrderId(1);
//        orderDetailDTOS.forEach(System.out::println);
    }
    @Test
    public void testFindByProductId(){
//        List<OrderDetailDTO> orderDetailDTOS = orderdetailServiceImp.findByProductId(3);
//        orderDetailDTOS.forEach(System.out::println);
    }
    @Test
    public void testSave(){
        Order order = new Order();
        Orderdetail orderdetail = new Orderdetail();
        Product product = new Product();

        product.setId(1);
        order.setId(1);

        orderdetail.setOrderID(order);
        orderdetail.setProductID(product);
        orderdetail.setQuantity(1);
        orderdetail.setUnitPrice(new BigDecimal("1.00"));
        orderdetail.setTotalPrice(new BigDecimal("1.00"));

        orderdetailServiceImp.createOrderdetail(orderdetail);
    }
}
