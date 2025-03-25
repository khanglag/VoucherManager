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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o JOIN FETCH o.userID WHERE o.userID.id = :userId")
    Page<Order> findByUserId(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT MAX(o.id) FROM Order o")
    int findMaxOrderId();

    Optional<Order> findById(int id);

    Page<Order> findByOrderDateBetween(Instant startDate, Instant endDate, Pageable pageable);

    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);
}

