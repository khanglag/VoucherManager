package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Model.Entity.Voucherusage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface VoucherusageRepository extends JpaRepository<Voucherusage, Integer>, JpaSpecificationExecutor<Voucher> {
    @Query("SELECT v.usedDate, COUNT(v.id) " +
            "FROM Voucherusage v " +
            "WHERE v.usedDate BETWEEN :startDate AND :endDate " +
            "GROUP BY v.usedDate " +
            "ORDER BY v.usedDate ASC")
    List<Object[]> findVoucherUsage(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT v FROM Voucherusage v WHERE YEAR(v.usedDate) = :year AND MONTH(v.usedDate) = :month")
    List<Voucherusage> findVouchersByMonthAndYear(int year, int month);
}
