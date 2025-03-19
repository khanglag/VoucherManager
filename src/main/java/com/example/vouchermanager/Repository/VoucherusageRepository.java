package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.Entity.Voucherusage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherusageRepository extends JpaRepository<Voucherusage, Integer> {
}
