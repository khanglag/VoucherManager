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

        //Lấy User
        User user = userRepository.findById(Math.toIntExact(request.getUserId()));
        BigDecimal TotalAmount = BigDecimal.ZERO;
        BigDecimal FinalAmount = BigDecimal.ZERO;
        for (Orderdetail orderdetail : request.getOrderdetails()) {
            TotalAmount = TotalAmount.add(orderdetail.getTotalPrice().multiply(BigDecimal.valueOf(orderdetail.getQuantity())));
        }
        FinalAmount = TotalAmount;
        List<Voucher> vouchers = new ArrayList<>();
        List<String> voucherCodes = request.getVoucherCodes(); // Giả sử đây là danh sách mã voucher

        for (String voucherCode : voucherCodes) {
            Optional<Voucher> optionalVoucher = voucherRepository.findById(voucherCode);
            if (optionalVoucher.isPresent()) {
                Voucher voucher = optionalVoucher.get();
                vouchers.add(voucher);
            }
        }
        for (Voucher voucher : vouchers) {
            if (voucher.getDiscountType()== DiscountType.FIXED || voucher.getDiscountType()== DiscountType.FREESHIP)
            {FinalAmount = FinalAmount.subtract(voucher.getDiscountValue());
                System.out.println("Trừ ship, trừ voucher:" + FinalAmount);}

            else if (voucher.getDiscountType()== DiscountType.PERCENTAGE){
                FinalAmount = FinalAmount.subtract(TotalAmount.multiply(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100)));
                System.out.println("Trừ phần trăm:" + FinalAmount);}

        }
        Order order = new Order();
        order.setUserID(user);
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setOrderDate(Instant.now());
        order.setTotalAmount(TotalAmount);
        order.setFinalAmount(FinalAmount);
        orderRepository.save(order);
        for (Orderdetail orderdetail : request.getOrderdetails()) {
            orderdetail.setOrderID(order);
            orderdetailRepository.save(orderdetail);
        }
        for (Voucher voucher : vouchers) {
            Voucherusage voucherUsage = new Voucherusage();
            voucherUsage.setVoucherCode(voucher);
            voucherUsage.setOrderID(order);
            voucherUsage.setUsedDate(Instant.now());
            voucherusageRepository.save(voucherUsage);
        }
        return null;
    }
}
