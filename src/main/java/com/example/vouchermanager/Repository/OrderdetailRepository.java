package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.Entity.Order;
import com.example.vouchermanager.Model.Entity.Orderdetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderdetailRepository extends JpaRepository<Orderdetail, Integer> {
<<<<<<< HEAD
//    List<Orderdetail> findByOrderID_Id(int orderId);
//    List<Orderdetail> findByProductID_Id(int productId);
    Page<Orderdetail> findByOrderID_Id(int orderId, Pageable pageable);
    Page<Orderdetail> findByProductID_Id(int productId, Pageable pageable);
=======
    List<Orderdetail> findByOrderID_Id(int orderId);
//    List<Orderdetail> findByProductID_Id(int productId);
    Page<Orderdetail> findByOrderID_Id(int orderId, Pageable pageable);
    Page<Orderdetail> findByProductID_Id(int productId, Pageable pageable);

>>>>>>> main
}
