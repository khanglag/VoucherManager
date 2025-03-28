package com.example.vouchermanager.Controllers;


import com.example.vouchermanager.Model.DTO.VoucherCreationResultDTO;
import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Model.Entity.*;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import com.example.vouchermanager.Service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private VoucherServiceImp voucherServiceImp;

    @Autowired
    private VoucherApplicableProductServiceImp voucherApplicableProductServiceImp;

    @Autowired
    private ProductServiceImp productServiceImp;

    @RequestMapping("/admin")
    public String vouchers() {
        return "admin/admin";
    }

    @GetMapping("/admin")
    public String checkLoginIndexPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                if (!user.getRoleID().getId().equals(1)) {
                    return "index";
                }
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<VoucherDTO> vouchers = voucherServiceImp.findAll();
        model.addAttribute("vouchers", vouchers);
//        System.out.println("-------------");
//
//        System.out.println(vouchers);
        int totalVouchers = voucherServiceImp.getTotalMaxUsage();
        int totalVouchersActive = voucherServiceImp.getRemainingUsageForActiveVouchers();
        int totalVouchersExpired = voucherServiceImp.getRemainingUsageForExpiredVouchers();
        int totalVouchersUsage = voucherServiceImp.getTotalUsedVouchers();
        int totalVouchersCancelled = voucherServiceImp.getTotalCancelledVoucherUsage();
        model.addAttribute("totalVouchersActive", totalVouchersActive);
        model.addAttribute("totalVouchersExpired", totalVouchersExpired);
        model.addAttribute("totalVouchersUsage",totalVouchersUsage);
        model.addAttribute("totalVouchers", totalVouchers);
        model.addAttribute("totalVouchersCancelled", totalVouchersCancelled);
        return "admin/admin";
    }

    @PostMapping(value = "/admin/addVoucher", consumes = "application/json")
    public ResponseEntity<?> createVoucher(@RequestBody VoucherDTO voucherDTO, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                Voucher voucher = new Voucher();
                voucher.setVoucherCode(voucherDTO.getVoucherCode());
                voucher.setTitle(voucherDTO.getTitle());
                voucher.setLogoUrl(voucherDTO.getLogoUrl());
                voucher.setDescription(voucherDTO.getDescription());
                voucher.setDiscountType(voucherDTO.getDiscountType());
                voucher.setDiscountValue(voucherDTO.getDiscountValue());
                voucher.setStartDate(voucherDTO.getStartDate());
                voucher.setEndDate(voucherDTO.getEndDate());
                voucher.setMinimumOrderValue(voucherDTO.getMinimumOrderValue());
                voucher.setStatus(VoucherStatus.ACTIVE);
                voucher.setCreatedBy(user);
                voucher.setUsageCount(voucherDTO.getUsageCount());
                voucher.setMaxUsage(voucherDTO.getMaxUsage());
                voucher.setCreatedDate(Instant.now());
                voucher.setApplicableForAllProducts(voucherDTO.isApplicableForAllProducts());
                try {
                    VoucherCreationResultDTO result = voucherServiceImp.createVoucherWithCustomCode(voucher);
                    log.info("Voucher tạo thành công, kết quả: {}", result); // Ghi log

                    if (result == null || result.getMessage() == null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("success", false, "message", "Lỗi khi tạo response!"));
                    }
                    return ResponseEntity.ok(Map.of(
                            "message", result.getMessage() != null ? result.getMessage() : "Không có message",
                            "voucher", result.getVoucher(),
                            "suggestions", result.getSuggestions() != null ? result.getSuggestions() : List.of()
                    ));
                } catch (Exception e) {
                    log.error("Lỗi khi trả về response: ", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("success", false, "message", "Mã voucher đã được sử dụng trước!!"));
                }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Voucher đã được tạo thành công!"));
    }

    @PostMapping(value = "/admin/editVoucher", consumes = "application/json")
    public ResponseEntity<?> editVoucher(@RequestBody Map<String, Object> data, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                try {
                String voucherCode = (String) data.get("voucherCode");
                String title = (String) data.get("title");
                String description = (String) data.get("description");
                DiscountType discountType = DiscountType.valueOf((String) data.get("discountType"));
                BigDecimal discountValue = BigDecimal.valueOf(((Number) data.get("discountValue")).doubleValue());
                BigDecimal minimumOrderValue = BigDecimal.valueOf(((Number) data.get("minimumOrderValue")).doubleValue());
                LocalDate startDate = LocalDate.parse((String) data.get("startDate"));
                LocalDate endDate = LocalDate.parse((String) data.get("endDate"));
                Integer maxUsage = (Integer) data.get("maxUsage");
                VoucherStatus status = VoucherStatus.valueOf((String) data.get("status"));

                List<Integer> appliedProducts = ((List<String>) data.get("appliedProducts"))
                        .stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
               Boolean applicableForAllProducts = false;
               Optional<Voucher> oldVoucher = voucherServiceImp.getById(voucherCode);


                Voucher updateVoucher = new Voucher();
                updateVoucher.setVoucherCode(voucherCode);
                updateVoucher.setTitle(title);
                updateVoucher.setDescription(description);
                updateVoucher.setDiscountType(discountType);
                updateVoucher.setDiscountValue(discountValue);
                updateVoucher.setStartDate(startDate);
                updateVoucher.setEndDate(endDate);
                updateVoucher.setStatus(status);
                updateVoucher.setMaxUsage(maxUsage);
                updateVoucher.setMinimumOrderValue(minimumOrderValue);
                updateVoucher.setApplicableForAllProducts(applicableForAllProducts);
                updateVoucher.setUsageCount(0);
                updateVoucher.setCreatedBy(user);
                updateVoucher.setCreatedDate(Instant.now());
                Voucherapplicableproduct applicableproduct = new Voucherapplicableproduct();
                if (appliedProducts == null || appliedProducts.isEmpty()) {
                    applicableForAllProducts= oldVoucher.get().getApplicableForAllProducts();
                }else{
                    for (int i = 0; i < appliedProducts.size(); i++) {
                        Optional<Product> pd = productServiceImp.getProductById(appliedProducts.get(i));
                        if (pd.isPresent()) {
                            Product product = pd.get();
                            // Tạo đối tượng id cho bảng trung gian
                            VoucherapplicableproductId id = new VoucherapplicableproductId();
                            id.setProductID(product.getId());
                            id.setVoucherCode(updateVoucher.getVoucherCode());

                            Voucherapplicableproduct applicableProduct = new Voucherapplicableproduct();
                            applicableProduct.setId(id);
                            applicableProduct.setProductID(product);
                            applicableProduct.setVoucherCode(updateVoucher);
                            voucherApplicableProductServiceImp.create(applicableProduct);
                        }else{
                            return ResponseEntity.ok(Map.of("success", false, "message", "Sản phẩm được áp dụng không tồn tại !!!"));
                        }
                    }
                }
                updateVoucher.setApplicableForAllProducts(applicableForAllProducts);
                voucherServiceImp.update(voucherCode,updateVoucher);

                    return ResponseEntity.ok(Map.of("success", true, "message", "Voucher đã được cập nhật thành công!"));
                }catch (Exception e) {

                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                                "success", false,
                                "message", "Lỗi khi cập nhật voucher: " + e.getMessage()
                        ));
                    }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Voucher đã được tạo thành công!"));
    }
}
