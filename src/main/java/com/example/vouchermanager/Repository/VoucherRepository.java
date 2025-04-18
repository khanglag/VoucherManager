package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.DTO.VoucherPerformanceDTO;
import com.example.vouchermanager.Model.Entity.User;
import com.example.vouchermanager.Model.Entity.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
    @Query("SELECT SUM(v.maxUsage) FROM Voucher v")
    Integer getTotalMaxUsage();

    // Tổng số lượt sử dụng còn lại của voucher đang hoạt động
    @Query("SELECT COALESCE(SUM(v.maxUsage - v.usageCount), 0) FROM Voucher v WHERE v.status = 'ACTIVE' AND :currentDate BETWEEN v.startDate AND v.endDate")
    Integer getRemainingUsageForActiveVouchers(LocalDate currentDate);

    // Tổng số lượt sử dụng còn lại của voucher sắp diễn ra
    @Query("SELECT COALESCE(SUM(v.maxUsage - v.usageCount), 0) FROM Voucher v WHERE v.status = 'UPCOMING' AND :currentDate < v.startDate")
    Integer getRemainingUsageForUpcomingVouchers(LocalDate currentDate);

    // Tổng số lượt sử dụng còn lại của voucher đã hết hạn
    @Query("SELECT COALESCE(SUM(v.maxUsage - v.usageCount), 0) FROM Voucher v WHERE v.status = 'EXPIRED' AND :currentDate > v.endDate")
    Integer getRemainingUsageForExpiredVouchers(LocalDate currentDate);

    // Tính tổng (maxUsage - usageCount) của các voucher bị hủy (CANCELLED)
    @Query("SELECT SUM(v.maxUsage - v.usageCount) FROM Voucher v WHERE v.status = 'CANCELLED'")
    Integer getTotalCancelledVoucherUsage();

    // Tính tổng usageCount của tất cả các voucher (tổng số voucher đã được sử dụng)
    @Query("SELECT SUM(v.usageCount) FROM Voucher v")
    Integer getTotalUsedVouchers();

    // Thống kê voucher tạo theo tháng
    @Query("SELECT YEAR(v.startDate) AS year, MONTH(v.startDate) AS month, COUNT(v) AS issuedCount " +
            "FROM Voucher v " +
            "GROUP BY YEAR(v.startDate), MONTH(v.startDate) " +
            "ORDER BY YEAR(v.startDate) DESC, MONTH(v.startDate) DESC")
    List<Object[]> countVouchersIssuedPerMonth();

    // Lấy tất cả voucher có trạng thái "ACTIVE"
    List<Voucher> findByStatus(String status);

    // Lấy số lượng voucher đã sử dụng và phát hành cho từng voucher
    @Query("SELECT v.voucherCode, v.usageCount, v.maxUsage FROM Voucher v WHERE v.status = 'ACTIVE'")
    List<Object[]> findVoucherUsageData();


    @Query("""
                SELECT v FROM Voucher v
                JOIN Voucherapplicableproduct vap ON v.voucherCode = vap.id.voucherCode
                WHERE vap.id.productID = :productId
                AND v.status = 'ACTIVE'
                AND v.discountType = 'PERCENTAGE'
            """)
    List<Voucher> findPercentageVouchersByProduct(@Param("productId") Integer productId);

    @Query("""
                SELECT v FROM Voucher v
                JOIN Voucherapplicableproduct vap ON v.voucherCode = vap.id.voucherCode
                WHERE vap.id.productID = :productId
                AND v.status = 'ACTIVE'
                AND v.discountType = 'FIXED'
            """)
    List<Voucher> findFixedVouchersByProduct(@Param("productId") Integer productId);

    @Query("""
                SELECT v FROM Voucher v
                JOIN Voucherapplicableproduct vap ON v.voucherCode = vap.id.voucherCode
                WHERE vap.id.productID = :productId
                AND v.status = 'ACTIVE'
                AND v.discountType = 'FREESHIP'
            """)
    List<Voucher> findFreeShipVouchersByProduct(@Param("productId") Integer productId);
    List<Voucher> findByApplicableForAllProductsTrue();

    List<Voucher> findByCreatedBy(User user);
}
