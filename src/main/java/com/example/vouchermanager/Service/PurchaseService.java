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
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {
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
    public Order processPurchase(PurchaseRequestDTO request) {

        //Lấy User
        User user = userRepository.findById(Math.toIntExact(request.getUserId()));
        BigDecimal TotalAmount = BigDecimal.ZERO;
        BigDecimal FinalAmount = BigDecimal.ZERO;
        for (Orderdetail orderdetail : request.getOrderdetails()) {
            TotalAmount = TotalAmount.add(orderdetail.getTotalPrice());
        }
        List<Voucher> vouchers = request.getVoucherCodes().stream()
                .map(voucherRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        for (Voucher voucher : vouchers) {
            if (voucher.getDiscountType()== DiscountType.FIXED)
                FinalAmount = TotalAmount.subtract(voucher.getDiscountValue()) ;
            else if (voucher.getDiscountType()== DiscountType.PERCENTAGE)
                FinalAmount = FinalAmount.multiply(voucher.getDiscountValue());
            else if (voucher.getDiscountType()== DiscountType.FREESHIP)
                FinalAmount = TotalAmount.subtract(BigDecimal.valueOf(30000));
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
        //Set xong của order
        // Set voucher
        //Set voucherusage
//        for (Voucher voucher : vouchers) {
//            Voucherusage voucherUsage = new Voucherusage();
//            voucherUsage.setVoucherCode(voucher);
//            voucherUsage.setOrderID(order);
//            voucherUsage.setUsedDate(Instant.now());
//            voucherusageRepository.save(voucherUsage);
//        }
        return null;
    }
}
