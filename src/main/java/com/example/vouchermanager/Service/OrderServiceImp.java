package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.MonthlyRevenueDTO;
import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.DTO.UserDTO;
import com.example.vouchermanager.Model.Entity.Order;
import com.example.vouchermanager.Model.Entity.User;
import com.example.vouchermanager.Model.Enum.OrderStatus;
import com.example.vouchermanager.Repository.OrderRepository;
import com.example.vouchermanager.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @Override
    public List<OrderDTO> findAllByUserId(int userId) {
        List<Order> orders = orderRepository.findByUserID_Id(userId);
        return orders.stream().map(order -> new OrderDTO(
                order.getId(),
                order.getUserID().getId(),
                order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                order.getTotalAmount(),
                order.getFinalAmount(),
                order.getOrderStatus()
        )).collect(Collectors.toList());
    }
@Override
public Page<OrderDTO> findAll(Pageable pageable) {
    return orderRepository.findAll(pageable)
            .map(order -> new OrderDTO(
                    order.getId(),
                    order.getUserID() != null ? order.getUserID().getId() : 0, // Kiểm tra null tránh lỗi
                    order.getOrderDate() != null ? order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null,
                    order.getTotalAmount(),
                    order.getFinalAmount(),
                    order.getOrderStatus()
            ));
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

//    @Override
//    public List<OrderDTO> findByStatus(String status) {
//        try {
//            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
//            return orderRepository.findByOrderStatus(orderStatus).stream()
//                    .map(order -> new OrderDTO(
//                            order.getId(),
//                            order.getUserID().getId(),
//                            order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
//                            order.getTotalAmount(),
//                            order.getFinalAmount(),
//                            order.getOrderStatus()
//                    ))
//                    .collect(Collectors.toList());
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ: " + status);
//        }
//    }

    @Override
    public Page<OrderDTO> findByStatus(String status, Pageable pageable) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByOrderStatus(orderStatus, pageable)
                    .map(order -> new OrderDTO(
                            order.getId(),
                            order.getUserID().getId(),
                            order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                            order.getTotalAmount(),
                            order.getFinalAmount(),
                            order.getOrderStatus()
                    ));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ: " + status);
        }
    }
    // Nho doc cmt
//    // Chuyển LocalDate -> Instant (bắt đầu và kết thúc ngày)
//    Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
//    Instant endInstant = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
//    @Override
//    public List<OrderDTO> getOrdersByDateRange(Instant startDate, Instant endDate) {
//        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
//                .map(order -> new OrderDTO(
//                        order.getId(),
//                        order.getUserID().getId(),
//                        order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
//                        order.getTotalAmount(),
//                        order.getFinalAmount(),
//                        order.getOrderStatus()
//                ))
//                .collect(Collectors.toList());
//    }
    @Override
    public Page<OrderDTO> getOrdersByDateRange(Instant startDate, Instant endDate, Pageable pageable) {
        return orderRepository.findByOrderDateBetween(startDate, endDate, pageable)
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUserID().getId(),
                        order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                        order.getTotalAmount(),
                        order.getFinalAmount(),
                        order.getOrderStatus()
                ));
    }

//    @Transactional
//    @Override
//    public List<OrderDTO> findByUserId(int userId) {
//        return orderRepository.findByUserId(userId).stream()
//                .map(order -> new OrderDTO(
//                        order.getId(),
//                        order.getUserID().getId(),
//                        order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
//                        order.getTotalAmount(),
//                        order.getFinalAmount(),
//                        order.getOrderStatus()
//                ))
//                .collect(Collectors.toList());
//    }

    @Transactional
    @Override
    public Page<OrderDTO> findByUserId(int userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUserID().getId(),
                        order.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                        order.getTotalAmount(),
                        order.getFinalAmount(),
                        order.getOrderStatus()
                ));
    }
    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(int id,Order order) {
        Order orderExits = orderRepository.findById(id).get();
        if(orderExits == null){
            return null;
        }

        if(order.getUserID() != null){
            orderExits.setUserID(order.getUserID());
        }
        if(order.getOrderDate() != null){
            orderExits.setOrderDate(order.getOrderDate());
        }
        if(order.getTotalAmount() != null){
            orderExits.setTotalAmount(order.getTotalAmount());
        }
        if(order.getFinalAmount() != null){
            orderExits.setFinalAmount(order.getFinalAmount());
        }
        if(order.getOrderStatus() != null){
            orderExits.setOrderStatus(order.getOrderStatus());
        }

        return orderRepository.save(orderExits);
    }

    public BigDecimal getTotalDiscountByMonth(int month, int year) {
        return orderRepository.getTotalDiscountByMonth(month, year);
    }
    @Override
    public BigDecimal getTotalFinalAmountForMonth(int month, int year) {
        return orderRepository.getTotalFinalAmountByMonthAndYear(month, year);
    }
    public List<MonthlyRevenueDTO> getMonthlyRevenue(int year) {
        List<Object[]> results = orderRepository.getMonthlyRevenue(year);
        List<MonthlyRevenueDTO> revenueList = new ArrayList<>();

        for (Object[] result : results) {
            int month = (int) result[0];
            BigDecimal totalAmount = (BigDecimal) result[1];
            revenueList.add(new MonthlyRevenueDTO(month, totalAmount));
        }

        return revenueList;
    }

}
