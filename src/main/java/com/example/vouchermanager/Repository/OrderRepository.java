package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.Entity.Order;
import com.example.vouchermanager.Model.Enum.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o JOIN FETCH o.userID WHERE o.userID.id = :userId")
    List<Order> findByUserId(@Param("userId") int userId);

    Optional<Order> findById(int id);

    List<Order> findByOrderDateBetween(Instant startDate, Instant endDate);

    List<Order> findByOrderStatus(OrderStatus status);
}

