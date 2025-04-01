package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.CartDTO;
import com.example.vouchermanager.Model.DTO.UpdateQuantityRequestDTO;
import com.example.vouchermanager.Model.Entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {
    @PostMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody UpdateQuantityRequestDTO request,
                                            HttpSession session) {
        AtomicBoolean success = new AtomicBoolean(false);
        List<CartDTO>cartDTOS= (List<CartDTO>) session.getAttribute("cartpayment");
        for (CartDTO cartDTO : cartDTOS) {
            if (cartDTO.getId()==(request.getItemId())) {
                System.out.println("ID CARTS:"+cartDTO.getId()+"|| ID REQUEST: "+request.getItemId());
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
}
