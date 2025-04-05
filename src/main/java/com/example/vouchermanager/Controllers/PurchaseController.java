package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.ApplyVoucher;
import com.example.vouchermanager.Model.DTO.CartDTO;
import com.example.vouchermanager.Model.DTO.PurchaseRequestDTO;
import com.example.vouchermanager.Model.Entity.Orderdetail;
import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Service.PurchaseService;
import com.example.vouchermanager.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private HttpSession session; // Để lấy session
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/create")
    public ResponseEntity<String> createPurchaseRequest( Authentication authentication) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu không có userId trong session
        }

        // Lấy cartpayment từ session
        List<CartDTO> cartDTOS = (List<CartDTO>) session.getAttribute("cartpayment");
        if (cartDTOS == null || cartDTOS.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu cart rỗng
        }

        // Lấy voucher từ session (giả sử voucher là danh sách String mã voucher)
        List<ApplyVoucher> voucherCode = (List<ApplyVoucher>) session.getAttribute("voucher");
        if (voucherCode == null) {
            voucherCode = new ArrayList<>(); // Nếu không có voucher thì khởi tạo danh sách rỗng
        }
        List<String> voucherCodes = new ArrayList<>();
        for (ApplyVoucher voucher : voucherCode) {
            if (voucher.getVoucherCode()!=null)
                voucherCodes.add(voucher.getVoucherCode());

        }
        List<Orderdetail> orderdetails = new ArrayList<>();

        for (CartDTO cartDTO : cartDTOS) {
            Orderdetail orderdetail = new Orderdetail();
            Product product = cartDTO.getProduct();
            // Thiết lập thông tin cho orderdetail
            orderdetail.setProductID(product);  // Set ID của sản phẩm
            orderdetail.setQuantity(cartDTO.getQuantity());  // Set số lượng
            // Tính tổng giá trị (giá sản phẩm * số lượng)
            orderdetail.setUnitPrice(product.getPrice());
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(cartDTO.getQuantity()));
            orderdetail.setTotalPrice(totalPrice);  // Set tổng giá trị
            // Thêm orderdetail vào danh sách
            orderdetails.add(orderdetail);
        }


        // Tạo PurchaseRequestDTO
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setUserId(Long.valueOf(user.getId()));
        purchaseRequestDTO.setOrderdetails(orderdetails);
        purchaseRequestDTO.setVoucherCodes(voucherCodes);
        purchaseService.processPurchase(purchaseRequestDTO);
        // Trả về response
        return ResponseEntity.ok("Purchase request created successfully");
    }
}
