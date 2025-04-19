//package com.example.vouchermanager.Service;
//
//import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
//import com.example.vouchermanager.Model.Entity.Order;
//import com.example.vouchermanager.Model.Entity.Orderdetail;
//import com.example.vouchermanager.Model.Entity.Product;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//public class OrderdetailServiceImpTest {
//    @Autowired
//    OrderdetailServiceImp orderdetailServiceImp;
//
//    @Test
//    public void testFindAll(){
////        List<OrderDetailDTO> orderDetailDTOS = orderdetailServiceImp.findAll();
////        orderDetailDTOS.forEach(System.out::println);
//        int page = 0;
//        int size = 10;
//
//    }
//    @Test
//    public void testFindById(){
//        OrderDetailDTO orderDetailDTO = orderdetailServiceImp.findById(1);
//        System.out.println(orderDetailDTO);
//    }
//    @Test
//    public void testFindByOrderId(){
////        List<OrderDetailDTO> orderDetailDTOS = orderdetailServiceImp.findByOrderId(1);
////        orderDetailDTOS.forEach(System.out::println);
//    }
//    @Test
//    public void testFindByProductId(){
////        List<OrderDetailDTO> orderDetailDTOS = orderdetailServiceImp.findByProductId(3);
////        orderDetailDTOS.forEach(System.out::println);
//    }
//    @Test
//    public void testSave(){
//        Order order = new Order();
//        Orderdetail orderdetail = new Orderdetail();
//        Product product = new Product();
//
//        product.setId(1);
//        order.setId(1);
//
//        orderdetail.setOrderID(order);
//        orderdetail.setProductID(product);
//        orderdetail.setQuantity(1);
//        orderdetail.setUnitPrice(new BigDecimal("1.00"));
//        orderdetail.setTotalPrice(new BigDecimal("1.00"));
//
//        orderdetailServiceImp.createOrderdetail(orderdetail);
//    }
//
//
//    @Test
//    public void testFindByOrderIds() {
//        int orderId = 1;  // ID của đơn hàng bạn muốn tìm
//        Pageable pageable = PageRequest.of(0, 10);  // Đặt trang hiện tại là 0 và số lượng phần tử trên mỗi trang là 10
//
//        // Gọi phương thức để lấy các chi tiết đơn hàng của đơn hàng có ID = 1
//        var orderDetails = orderdetailServiceImp.findByOrderId(orderId, pageable);
//
//        // Kiểm tra xem kết quả trả về không null
//        assertNotNull(orderDetails);
//
//        // In ra các thông tin chi tiết của đơn hàng
//        orderDetails.forEach(orderDetailDTO -> {
//            System.out.println("OrderDetail ID: " + orderDetailDTO.getOrderId());
//            System.out.println("Product ID: " + orderDetailDTO.getProductId());
//            System.out.println("Quantity: " + orderDetailDTO.getQuantity());
//            System.out.println("Unit Price: " + orderDetailDTO.getUnitPrice());
//            System.out.println("Total Price: " + orderDetailDTO.getTotalPrice());
//        });
//    }
//}
