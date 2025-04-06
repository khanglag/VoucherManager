package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.PurchaseRequestDTO;
import com.example.vouchermanager.Model.Entity.*;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.OrderStatus;
import com.example.vouchermanager.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImp implements PurchaseService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private OrderdetailRepository orderdetailRepository;
    @Autowired
    VoucherService voucherService ;
    @Autowired
    VoucherusageRepository voucherusageRepository;
    @Override
    public Order processPurchase(PurchaseRequestDTO request) {
        // Lấy User
        User user = userRepository.findById(Math.toIntExact(request.getUserId()));

        // Tính tổng tiền đơn hàng
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Orderdetail orderDetail : request.getOrderdetails()) {
            BigDecimal itemTotal = orderDetail.getTotalPrice()
                    .multiply(BigDecimal.valueOf(orderDetail.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // Tính tổng sau khi áp dụng voucher
        BigDecimal finalAmount = totalAmount;
        List<Voucher> vouchers = new ArrayList<>();

        for (String voucherCode : request.getVoucherCodes()) {
            voucherRepository.findById(voucherCode).ifPresent(vouchers::add);
        }

        for (Voucher voucher : vouchers) {
            switch (voucher.getDiscountType()) {
                case FIXED:
                case FREESHIP:
                    finalAmount = finalAmount.subtract(voucher.getDiscountValue());
                    break;
                case PERCENTAGE:
                    BigDecimal discount = totalAmount.multiply(voucher.getDiscountValue())
                            .divide(BigDecimal.valueOf(100));
                    finalAmount = finalAmount.subtract(discount);
                    break;
                default:
                    break;
            }
        }
        finalAmount=finalAmount.add(BigDecimal.valueOf(30000));
        // Tạođơn hàng
        Order order = new Order();
        order.setUserID(user);
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setOrderDate(Instant.now());
        order.setTotalAmount(totalAmount);
        order.setFinalAmount(finalAmount);
        orderRepository.save(order);
        // Lưu chi tiết đơn hàng
        for (Orderdetail orderDetail : request.getOrderdetails()) {
            orderDetail.setOrderID(order);
            orderdetailRepository.save(orderDetail);
        }
        // Lưu thông tin sử dụng voucher
        for (Voucher voucher : vouchers) {
            Voucherusage usage = new Voucherusage();
            usage.setVoucherCode(voucher);
            usage.setOrderID(order);
            usage.setUsedDate(Instant.now());
            voucherusageRepository.save(usage);
        }

        return order;
    }

}
