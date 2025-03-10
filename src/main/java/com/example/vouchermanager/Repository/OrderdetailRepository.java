package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.Entity.Orderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderdetailRepository extends JpaRepository<Orderdetail, Integer> {
}
