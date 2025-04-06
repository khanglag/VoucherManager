package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.ApplyVoucher;
import com.example.vouchermanager.Model.DTO.CartDTO;
import com.example.vouchermanager.Model.DTO.PurchaseRequestDTO;
import com.example.vouchermanager.Model.Entity.Orderdetail;
import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Repository.VoucherRepository;
import com.example.vouchermanager.Service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.net.Authenticator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class VNPAYController {
    @Autowired
    VNPAYService vnpayService;
    @Autowired
    OrderService orderService;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    UserService userService;
    @Autowired
    PurchaseService purchaseService;
    @GetMapping("/createOrderVNPay")
    public String createOrderPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            model.addAttribute("user", userDetails.getUsername());
            BigDecimal totalAmount = BigDecimal.ZERO;

// Lấy cartpayment từ session
            List<CartDTO> cartItems = (List<CartDTO>) session.getAttribute("cartpayment");
// Tính tổng tiền hàng
            for (CartDTO item : cartItems) {
                BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }

// Lấy danh sách mã voucher đã chọn
            List<ApplyVoucher> applyVouchers = (List<ApplyVoucher>) session.getAttribute("voucher");
            List<String> voucherCodes = new ArrayList<>();
            if (applyVouchers != null) {
                for (ApplyVoucher applyVoucher : applyVouchers) {
                    if (applyVoucher.getVoucherCode() != null) {
                        voucherCodes.add(applyVoucher.getVoucherCode());
                    }
                }
            }

// Lấy thông tin voucher từ database
            List<Voucher> vouchers = new ArrayList<>();
            for (String code : voucherCodes) {
                voucherRepository.findById(code).ifPresent(vouchers::add);
            }

// Tính tổng sau khi áp dụng voucher
            BigDecimal finalAmount = totalAmount;
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
// Đảm bảo giá trị không âm
            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                finalAmount = BigDecimal.ZERO;
            }
            model.addAttribute("total", finalAmount);
            session.setAttribute("total", finalAmount+"");
        }
            return "createOrder";
    }
    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request, HttpSession session) {
        try {
            Object totalObj = session.getAttribute("total");
            if (totalObj == null) {
                throw new IllegalArgumentException("Total amount is missing in the session.");
            }

            // Chuyển đổi tổng tiền từ String sang double
            double totalAmount = Double.parseDouble(totalObj.toString());
            int totalAmountInt = (int) totalAmount;

            // Lấy URL cơ sở (base URL) của server
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

            // Tạo URL VNPAY
            String vnpayUrl = vnpayService.createOrder(request, totalAmountInt, orderInfo, baseUrl);

            // Kiểm tra nếu URL đã có http:// hoặc https://
            if (!vnpayUrl.startsWith("http://") && !vnpayUrl.startsWith("https://")) {
                vnpayUrl = "https://" + vnpayUrl;  // Nếu không có http:// hoặc https://, tự thêm https://
            }
            vnpayUrl = vnpayUrl.replace("https:// ", "");
            System.out.println(vnpayUrl);
            return "redirect:" + vnpayUrl;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "redirect:/error?message=Invalid total amount";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/error?message=" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error?message=An error occurred while processing the order";
        }
    }


    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String handleVnpayReturn(@RequestParam Map<String, String> allParams, Model model,Authentication authentication,HttpSession session) {
        System.out.println("VNPAY RETURN PARAMS: " + allParams);
        String vnpResponseCode = allParams.get("vnp_ResponseCode");
        if ("00".equals(vnpResponseCode)) {
            model.addAttribute("message", "Thanh toán thành công!");
        } else {
            model.addAttribute("message", "Thanh toán thất bại!");
        }
        model.addAttribute("amount", allParams.get("vnp_Amount"));
        model.addAttribute("orderInfo", allParams.get("vnp_OrderInfo"));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

        // Lấy cartpayment từ session
        List<CartDTO> cartDTOS = (List<CartDTO>) session.getAttribute("cartpayment");
        List<ApplyVoucher> voucherCode = (List<ApplyVoucher>) session.getAttribute("voucher");
        List<String> voucherCodes = new ArrayList<>();
        for (ApplyVoucher voucher : voucherCode) {
            if (voucher.getVoucherCode()!=null)
                voucherCodes.add(voucher.getVoucherCode());
        }
        List<Orderdetail> orderdetails = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOS) {
            Orderdetail orderdetail = new Orderdetail();
            Product product = cartDTO.getProduct();
            orderdetail.setProductID(product);  // Set ID của sản phẩm
            orderdetail.setQuantity(cartDTO.getQuantity());  // Set số lượng
            orderdetail.setUnitPrice(product.getPrice());
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(cartDTO.getQuantity()));
            orderdetail.setTotalPrice(totalPrice);  // Set tổng giá trị
            orderdetails.add(orderdetail);
        }
        // Tạo PurchaseRequestDTO
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setUserId(Long.valueOf(user.getId()));
        purchaseRequestDTO.setOrderdetails(orderdetails);
        purchaseRequestDTO.setVoucherCodes(voucherCodes);
        purchaseService.processPurchase(purchaseRequestDTO);
        // Trả về response
        session.setAttribute("voucher", null);
        session.setAttribute("cartpayment", null);
        return "/individual/order_history";
    }

}
