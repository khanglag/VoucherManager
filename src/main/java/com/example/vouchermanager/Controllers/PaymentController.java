package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.ApplyVoucher;
import com.example.vouchermanager.Model.DTO.CartDTO;
import com.example.vouchermanager.Model.DTO.UpdateQuantityRequestDTO;
import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    VoucherService voucherService;
    @PostMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody UpdateQuantityRequestDTO request,
                                            HttpSession session) {
        AtomicBoolean success = new AtomicBoolean(false);
        List<CartDTO>cartDTOS= (List<CartDTO>) session.getAttribute("cartpayment");
        for (CartDTO cartDTO : cartDTOS) {
            if (cartDTO.getId()==(request.getItemId())) {
                cartDTO.setQuantity(cartDTO.getQuantity() + request.getQuantity());
                success.set(true);
                session.setAttribute("cartpayment", cartDTOS);
                break;
            }
        }
        if (success.get()) {
            return ResponseEntity.ok(Map.of("success", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Cập nhật thất bại!"));
        }
    }
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable int cartItemId, HttpSession session) {
        // Lấy danh sách cart từ session
        List<CartDTO> cartDTOS = (List<CartDTO>) session.getAttribute("cartpayment");
        List<Product> products = (List<Product>) session.getAttribute("cart");
        if (products != null) {
            Product productToRemove = null;

            // Tìm sản phẩm cần xóa
            for (CartDTO cartDTO : cartDTOS) {
                if (cartDTO.getId() == cartItemId) {
                    productToRemove = cartDTO.getProduct();
                    break; // Dừng vòng lặp ngay khi tìm thấy
                }
            }

            if (productToRemove != null) {
                // Dùng Iterator để xóa phần tử an toàn
                Iterator<Product> iterator = products.iterator();
                while (iterator.hasNext()) {
                    Product product = iterator.next();
                    if (product.getId() == productToRemove.getId()) {
                        iterator.remove();
                    }
                }
                session.setAttribute("cart", products);
            }
        }
        if (cartDTOS != null) {
            // Xóa phần tử có id khớp với cartItemId
            boolean removed = cartDTOS.removeIf(cartDTO -> cartDTO.getId() == cartItemId);

            // Debugging (tùy chọn)
            if (removed) {
                System.out.println("Đã xóa CartDTO với ID: " + cartItemId);
            } else {
                System.out.println("Không tìm thấy CartDTO với ID: " + cartItemId);
            }
            // Cập nhật lại session sau khi xóa
            session.setAttribute("cartpayment", cartDTOS);
        } else {
            System.out.println("Giỏ hàng trong session là null");
        }

        // Trả về 200 OK
        return ResponseEntity.ok().build();
    }
    @PostMapping("/apply-voucher")
    @ResponseBody
    public ResponseEntity<String> applyVoucher(@RequestBody Map<String, String> payload, HttpSession session) {
        try {
            String voucherCode = payload.get("voucherCode"); // Lấy voucherCode từ body
            List<ApplyVoucher> vouchers = (List<ApplyVoucher>) session.getAttribute("voucher");
            if(voucherService.getById(voucherCode).get().getDiscountType()== DiscountType.FREESHIP){
                for (ApplyVoucher applyVoucher : vouchers) {
                    if(applyVoucher.getVoucherstyle()=="ship")
                        applyVoucher.setVoucherCode(voucherCode);
                    else  applyVoucher.setVoucherCode(voucherCode);
                }
            };
            session.setAttribute("voucher", vouchers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error applying voucher: " + e.getMessage());
        }
        return null;
    }
    @GetMapping("/cancelvoucher")
    public ResponseEntity<String> cancelVoucher(HttpSession session) {
        try {
            List<ApplyVoucher> applyVouchers =(List<ApplyVoucher>) session.getAttribute("voucher");

            System.out.println("Voucher đã được hủy thành công");

            return ResponseEntity.ok("Voucher đã được hủy thành công");
        } catch (Exception e) {
            // Nếu có lỗi, trả về mã lỗi HTTP 500 (Internal Server Error)
            return ResponseEntity.status(500).body("Đã xảy ra lỗi khi hủy voucher");
        }
    }
}
