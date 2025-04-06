package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import com.example.vouchermanager.Model.DTO.VoucherCreationResultDTO;
import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Model.Entity.*;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import com.example.vouchermanager.Repository.ProductRepository;
import com.example.vouchermanager.Repository.VoucherRepository;
import com.example.vouchermanager.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.*;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderdetailService orderdetailService;

    @Autowired
    private VoucherServiceImp voucherServiceImp;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherusageServiceImp voucherusageServiceImp;

    @GetMapping("/individual")
    public String checkLoginIndividualPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

            if (user != null) {
                model.addAttribute("userRole", user.getRoleID().getId());
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);
        return "user/individual";
    }

    @GetMapping("/individual/personal_infomation")
    public String checkLoginPersonalInfomationPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

            if (user != null) {
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
                model.addAttribute("infoUser", user);
                model.addAttribute("userRole", user.getRoleID().getId());
            }
        }
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);
        return "user/personal_infomation";
    }
    @GetMapping("/individual/order_history")
    public String checkLoginOrderHistoryPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            int userid= user.getId();
            if (user != null) {

                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
                model.addAttribute("userRole", user.getRoleID().getId());
            }
            List<OrderDTO> orders = orderService.findAllByUserId(userid);
            model.addAttribute("orders", orders);

            Map<Integer, List<OrderDetailDTO>> orderDetailsMap = new HashMap<>();
            Map<Integer, List<Product>> productDetailsMap = new HashMap<>();
            Map<Integer, List<Voucherusage>> voucherUsageMap = new HashMap<>();

            for (OrderDTO order : orders) {
                Integer orderId = order.getOrderId();
                List<OrderDetailDTO> details = orderdetailService.findByOrderId(order.getOrderId());
                orderDetailsMap.put(order.getOrderId(), details);
                List<Product> products = new ArrayList<>();
                for (OrderDetailDTO detail : details) {
                    Optional<Product> product = productService.getProductById(detail.getProductId());
                    product.ifPresent(products::add);
                }
                productDetailsMap.put(order.getOrderId(), products);
                // Voucher Usages
                List<Voucherusage> voucherUsages = voucherusageServiceImp.getVoucherUsagesByOrderId(orderId);
                voucherUsageMap.put(orderId, voucherUsages);
            }

            model.addAttribute("orderDetailsMap", orderDetailsMap);
            model.addAttribute("productDetailsMap", productDetailsMap);
            model.addAttribute("voucherUsageMap", voucherUsageMap);
        }

        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);


        return "user/order_history";
    }
    @GetMapping("/individual/vouchersmanager")
    public String checkLoginVoucherManagerPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                if (user.getRoleID().getId() != 2) {
                    return "redirect:/individual";
                }
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
                model.addAttribute("userRole", user.getRoleID().getId());

                List<Voucher> vouchersByStaff = voucherRepository.findByCreatedBy(user);
                model.addAttribute("vouchersByStaff", vouchersByStaff);

            }
        }
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);


        return "user/vouchersmanager";
    }

    @PostMapping(value = "/individual/personal_infomation/editUser", consumes = "application/json")
    public ResponseEntity<?> editUser(@RequestBody Map<String, Object> data, Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

            if (user != null) {
                try {
                    String receivedUsername = (String) data.get("username");
                    User existingUser = userService.findByUsername(receivedUsername);
                    if (existingUser == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Người dùng không tồn tại"));
                    }

                    String fullName = (String) data.get("fullName");
                    String email = (String) data.get("email");
                    String phone = (String) data.get("phoneNumber");
                    String roleid = (String) data.get("roleId");
                    String statusStr = (String) data.get("status");
                    Boolean status = "1".equals(statusStr);
                    if (!existingUser.getEmail().equals(email) && userService.findByEmail(email) != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email đã được sử dụng"));
                    }

                    if (!existingUser.getPhoneNumber().equals(phone) && userService.findByPhoneNumber(phone) != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Số điện thoại đã được sử dụng"));
                    }

                    existingUser.setFullName(fullName);
                    existingUser.setEmail(email);
                    existingUser.setPhoneNumber(phone);

                    Role role = new Role();
                    role.setId(Integer.parseInt(roleid));
                    existingUser.setRoleID(role);
                    existingUser.setStatus(status);
                    userServiceImp.updateUser(existingUser.getId(), existingUser);
                    return ResponseEntity.ok(Map.of("message", "Cập nhật người dùng thành công!"));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                            "error", "Cập nhật thất bại: " + e.getMessage()
                    ));
                }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật người dùng thành công"));
    }

    @PostMapping("/individual/personal_infomation/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Bạn chưa đăng nhập"));
        }

        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = userService.findByUsername(username);
        user.setStatus(true);
        System.out.println(user.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy người dùng"));
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Mật khẩu cũ không chính xác"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userServiceImp.updateUser(user.getId(), user);

        return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công"));
    }

    @PostMapping(value = "/individual/vouchersmanager/addVoucher", consumes = "application/json")
    public ResponseEntity<?> createVoucher(@RequestBody VoucherDTO voucherDTO, Model model, Authentication authentication) {
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
}
