package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.Entity.Order;
import com.example.vouchermanager.Model.Enum.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o JOIN FETCH o.userID WHERE o.userID.id = :userId")
    Page<Order> findByUserId(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT MAX(o.id) FROM Order o")
    int findMaxOrderId();
    List<Order> findByUserID_Id(int userId);
    Optional<Order> findById(int id);

    Page<Order> findByOrderDateBetween(Instant startDate, Instant endDate, Pageable pageable);

    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.totalAmount - o.finalAmount), 0) " +
            "FROM Order o " +
            "WHERE FUNCTION('MONTH', o.orderDate) = :month " +
            "AND FUNCTION('YEAR', o.orderDate) = :year " +
            "AND o.orderStatus = 'COMPLETED'")
    BigDecimal getTotalDiscountByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.orderStatus = 'COMPLETED' AND FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('YEAR', o.orderDate) = :year")
    BigDecimal getTotalFinalAmountByMonthAndYear(int month, int year);

    @Query("SELECT MONTH(o.orderDate) AS month, SUM(o.finalAmount) AS total " +
            "FROM Order o " +
            "WHERE YEAR(o.orderDate) = :year AND o.orderStatus = 'COMPLETED' " +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)")
    List<Object[]> getMonthlyRevenue(int year);
    Order findTopByOrderByIdDesc();
}

