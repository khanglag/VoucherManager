package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.Entity.Order;
import com.example.vouchermanager.Model.Entity.Orderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderdetailRepository extends JpaRepository<Orderdetail, Integer> {
    List<Orderdetail> findByOrderID_Id(int orderId);
    List<Orderdetail> findByProductID_Id(int productId);
}
