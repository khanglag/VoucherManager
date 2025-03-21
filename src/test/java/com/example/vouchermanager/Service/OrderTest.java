//package com.example.vouchermanager.Service;
//
//import com.example.vouchermanager.Model.DTO.OrderDTO;
//import com.example.vouchermanager.Model.Entity.Order;
//import com.example.vouchermanager.Model.Entity.User;
//import com.example.vouchermanager.Model.Enum.OrderStatus;
//import jakarta.persistence.Id;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.List;
//
//@SpringBootTest
//public class OrderTest {
//    @Autowired
//    OrderServiceImp orderService;
//
////    @Test
////    public void test() {
////        List<OrderDTO> orders = orderService.findAll();
////        orders.forEach(order -> System.out.println(order));
////    }
//
//    @Test
//    public void testFindByID(){
//        OrderDTO order = orderService.findById(1);
//        System.out.println(order);
//    }
//
////    @Test
////    public void testFindByUserId(){
////        List<OrderDTO> orders = orderService.findByUserId(2);
////        orders.forEach(order -> System.out.println(order));
////    }
////
////    @Test
////    public void testFindByRangeDate(){
////        // Chuyển LocalDate -> Instant (bắt đầu và kết thúc ngày)
////        LocalDate startDate = LocalDate.of(2025, 2, 1);
////        LocalDate endDate = LocalDate.of(2025, 3, 30);
////        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
////        Instant endInstant = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
////        List<OrderDTO> orderDTOS = orderService.getOrdersByDateRange(startInstant, endInstant);
////        orderDTOS.forEach(order -> System.out.println(order));
////    }
//
////    @Test
////    public void testFindByStatus(){
////        System.out.println(orderService.findByStatus("completed"));
////    }
//
//    @Test
//    public void testCreateOrder(){
//        Order order = new Order();
//        User user = new User();
//        user.setId(2);
//        OrderStatus orderStatus = OrderStatus.PENDING;
//
//        order.setUserID(user);
//        order.setOrderStatus(orderStatus);
//        //order.setOrderDate(Instant.now().atZone(ZoneId.of("Asia/Bangkok")).toInstant());
//        order.setTotalAmount(new BigDecimal(0));
//        order.setFinalAmount(new BigDecimal(0));
//
//        Order order1 = orderService.createOrder(order);
//        System.out.println(order1);
//    }
//
//    @Test
//    public void testUpdateOrder(){
//        Order order = new Order();
//        order.setOrderDate(Instant.now());
//        order.setOrderStatus(OrderStatus.CANCELLED);
//        order.setFinalAmount(new BigDecimal(100));
//
//        Order order1 = orderService.updateOrder(9,order);
//    }
//}
