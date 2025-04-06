package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.CartDTO;
import com.example.vouchermanager.Service.OrderService;
import com.example.vouchermanager.Service.VNPAYService;
import com.example.vouchermanager.Service.VoucherService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/createOrder")
    public String createOrderPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("user", userDetails.getUsername());
            BigDecimal total = orderService.getLastOrder().getTotalAmount();
            model.addAttribute("total", total);
            session.setAttribute("total", total+"");
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
    public String handleVnpayReturn(@RequestParam Map<String, String> allParams, Model model) {
        // allParams chứa tất cả các tham số VNPAY trả về (bao gồm vnp_Amount, vnp_OrderInfo, vnp_SecureHash...)
        System.out.println("VNPAY RETURN PARAMS: " + allParams);

        // Ví dụ kiểm tra mã phản hồi từ VNPAY (kiểm tra vnp_ResponseCode)
        String vnpResponseCode = allParams.get("vnp_ResponseCode");
        if ("00".equals(vnpResponseCode)) {
            model.addAttribute("message", "Thanh toán thành công!");
        } else {
            model.addAttribute("message", "Thanh toán thất bại!");
        }

        // Hiển thị số tiền và thông tin đơn hàng từ các tham số trả về
        model.addAttribute("amount", allParams.get("vnp_Amount"));
        model.addAttribute("orderInfo", allParams.get("vnp_OrderInfo"));

        return "vnpay_result"; // Trang kết quả thanh toán (phải có file vnpay_result.html)
    }

}
