package com.example.vouchermanager.Controllers;


import com.example.vouchermanager.Model.DTO.*;
import com.example.vouchermanager.Model.Entity.*;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.OrderStatus;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import com.example.vouchermanager.Repository.OrderRepository;
import com.example.vouchermanager.Service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
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

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private OrderServiceImp orderServiceImp;

    @Autowired
    private OrderdetailServiceImp orderdetailServiceImp;

    @Autowired
    private OrderdetailService orderdetailService;

    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping("/admin")
    public String vouchers() {
        return "admin/admin";
    }

    @GetMapping("/admin")
    public String checkLoginIndexPage(Model model, Authentication authentication, HttpSession session,
                                      @RequestParam(value = "month", required = false) Integer month,
                                      @RequestParam(value = "year", required = false) Integer year) {
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
        List<ProductDTO> products = productServiceImp.findAll();
        // Lấy danh sách đơn hàng
        Pageable pageable = PageRequest.of(0, 10, Sort.by("orderDate").descending());
        Page<OrderDTO> orders = orderServiceImp.findAll(pageable);
        Map<Integer, String> userNames = new HashMap<>();
        for (OrderDTO order : orders) {
            int userId = order.getUserId();
            if (!userNames.containsKey(userId)) {
                com.example.vouchermanager.Model.Entity.User user = userService.findById(userId);
                userNames.put(userId, user != null ? user.getFullName() : "N/A");
            }
        }
        model.addAttribute("userNames", userNames);
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("vouchers", vouchers);
        model.addAttribute("products", products);

        int totalVouchers = voucherServiceImp.getTotalMaxUsage();
        int totalVouchersActive = voucherServiceImp.getRemainingUsageForActiveVouchers();
        int totalVouchersExpired = voucherServiceImp.getRemainingUsageForExpiredVouchers();
        int totalVouchersUsage = voucherServiceImp.getTotalUsedVouchers();
        int totalVouchersCancelled = (voucherServiceImp.getTotalCancelledVoucherUsage() != null)
                ? voucherServiceImp.getTotalCancelledVoucherUsage()
                : 0;
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("totalVouchersActive", totalVouchersActive);
        model.addAttribute("totalVouchersExpired", totalVouchersExpired);
        model.addAttribute("totalVouchersUsage",totalVouchersUsage);
        model.addAttribute("totalVouchers", totalVouchers);
        model.addAttribute("totalVouchersCancelled", totalVouchersCancelled);

        // Thống kê số voucher đã tạo theo tháng
        Map<String, Object> statistics = voucherServiceImp.getVoucherStatistics();
        @SuppressWarnings("unchecked")
        List<Object[]> issuedPerMonth = (List<Object[]>) statistics.get("issuedPerMonth");
        List<String> months = new ArrayList<>();
        List<Integer> issuedCounts = new ArrayList<>();
        for (Object[] row : issuedPerMonth) {
            int a = (int) row[1];
            int issuedCount = row[2] != null ? ((Number) row[2]).intValue() : 0;
            months.add(getMonthName(a));
            issuedCounts.add(issuedCount);
        }
        model.addAttribute("months", months);
        model.addAttribute("issuedCounts", issuedCounts);
        // Thống kê hiệu suất của từng voucher
        Map<String, BigDecimal> performanceMap = voucherServiceImp.getVoucherPerformance();
        model.addAttribute("performanceMap", performanceMap);

        // Lấy tháng hiện tại nếu không có giá trị từ request
        if (month == null || year == null) {
            LocalDate today = LocalDate.now();
            month = today.getMonthValue();
            year = today.getYear();
        }

        BigDecimal totalDiscount = orderServiceImp.getTotalDiscountByMonth(month, year);
        model.addAttribute("totalDiscount", totalDiscount);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);

        //Thống kê hiệu suất voucher của nhân viên tạo
        List<VoucherPerformanceDTO> performanceList = voucherServiceImp.calculateVoucherPerformanceForAllUsersWithRoleId2();
        model.addAttribute("performanceList", performanceList);

        //Thống kê hiệu suất voucher của admin
        List<VoucherPerformanceDTO> performanceListByAdmin = voucherServiceImp.calculateVoucherPerformanceForAllUsersWithRoleId1();
        System.out.println("Performance List performanceListByAdmin: " + performanceListByAdmin);
        model.addAttribute("performanceListByAdmin", performanceListByAdmin);

        return "admin/admin";
    }
    private String getMonthName(int month) {
        switch (month) {
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return "";
        }
    }



    @PostMapping("/admin")

    public ResponseEntity<Map<String, BigDecimal>> getStatistics(@RequestParam int month, @RequestParam int year,
                                                                 @RequestParam(required = false) Boolean isDiscount) {
        Map<String, BigDecimal> response = new HashMap<>();

        // Kiểm tra nếu là thống kê giảm giá
        if (isDiscount != null && isDiscount) {
            BigDecimal totalDiscount = orderServiceImp.getTotalDiscountByMonth(month, year);
            response.put("totalDiscount", totalDiscount);
        } else {
            // Nếu không phải thống kê giảm giá, xử lý thống kê doanh thu thực tế
            BigDecimal totalRevenue = orderServiceImp.getTotalFinalAmountForMonth(month, year);
            response.put("totalRevenue", totalRevenue);
        }

        return ResponseEntity.ok(response);
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
                            List<VoucherDTO> vouchers = voucherServiceImp.findAll();
                            model.addAttribute("vouchers", vouchers);
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

    @PostMapping(value = "/admin/addUser", consumes = "application/json")
    public ResponseEntity<?> addUser(@RequestBody Map<String, Object> data, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                try {

                    String receivedUsername = (String) data.get("username");
                    String fullName = (String) data.get("fullName");
                    String email = (String) data.get("email");
                    String phone = (String) data.get("phoneNumber");
                    String roleid = (String) data.get("role");
                    System.out.println(roleid);
                    if (userServiceImp.findByPhoneNumber(phone) != null) {
                        System.out.println("sdt đã tồn tại");
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("error", "Số điện thoại đã được sử dụng!"));
                    }

                    if (userServiceImp.findByEmail(email) != null) {
                        System.out.println("email đã tồn tại");
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("error", "Email đã được sử dụng!"));
                    }
                    User u = new User();
                    u.setUsername(receivedUsername);
                    u.setFullName(fullName);
                    u.setEmail(email);
                    u.setPhoneNumber(phone);
                    u.setPassword("123456");
                    Role role = new Role();
                   role.setId(Integer.parseInt(roleid));
                   System.out.println(role.getId());
                    u.setRoleID(role);
                    u.setStatus(true);
                   System.out.println("--------------------");
                   System.out.println(u);
                   userServiceImp.createUser(u);
                    return ResponseEntity.ok(Map.of("message", "Thêm người dùng thành công!"));
                }catch (Exception e) {

                    return ResponseEntity.ok(Map.of("message", "Thêm người thất bại do trùng username!"));
                }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Thêm người dùng thành công"));
    }

    @PostMapping(value = "/admin/editUser", consumes = "application/json")
    public ResponseEntity<?> editUser(@RequestBody Map<String, Object> data, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

            if (user != null) {
                try {
                    String receivedUsername = (String) data.get("username");
                    // Tìm user cần sửa
                    User existingUser = userService.findByUsername(receivedUsername);
                    if (existingUser == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Người dùng không tồn tại"));
                    }

                    String fullName = (String) data.get("fullName");
                    String email = (String) data.get("email");
                    String phone = (String) data.get("phoneNumber");
                    String roleid = (String) data.get("role");
                    String statusStr = (String) data.get("status");
                    Boolean status = "1".equals(statusStr);

                    // Kiểm tra nếu email thay đổi thì phải kiểm tra trùng lặp
                    if (!existingUser.getEmail().equals(email) && userService.findByEmail(email) != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email đã được sử dụng"));
                    }

                    // Kiểm tra nếu số điện thoại thay đổi thì phải kiểm tra trùng lặp
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

    @PostMapping(value = "/admin/blockUser", consumes = "application/json")
    public ResponseEntity<?> blockUser(@RequestBody Map<String, Object> data, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

            if (user != null) {
                try {
                    User existingUser = userService.findByUsername( (String) data.get("username"));
                    System.out.println(existingUser.getEmail());
                    existingUser.setStatus(false);
                    userServiceImp.updateUser(existingUser.getId(), existingUser);
                    return ResponseEntity.ok(Map.of("message", "Khóa người dùng thành công"));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                            "error", "Cập nhật thất bại: " + e.getMessage()
                    ));
                }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Khóa người dùng thành công"));
    }

    @PostMapping(value = "/admin/addProduct", consumes = "application/json")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> data, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                try {
                    System.out.println("----------data----------");
                    System.out.println(data);
                    String productName = (String) data.get("productName");
                    int productPrice = (Integer) data.get("price");
                    String productUrlImage = (String) data.get("imageUrl");
                    String productStatus = (String) data.get("status");
                    boolean isActive = "1".equals(productStatus);
                    Product product = new Product();
                    product.setProductName(productName);
                    product.setPrice(BigDecimal.valueOf(productPrice));
                    product.setImageUrl(productUrlImage);
                    product.setStatus(isActive);
                    productServiceImp.createProduct(product);
                    return ResponseEntity.ok(Map.of("message", "Thêm sản phẩm mới thành công"));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                            "error", "Thêm sản phẩm mới thất bại: " + e.getMessage()
                    ));
                }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Thêm sản phẩm mới thành công"));
    }

    @PostMapping(value = "/admin/editProduct", consumes = "application/json")
    public ResponseEntity<?> editProduct(@RequestBody Map<String, Object> data, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                try {
                    System.out.println("----------data----------");
                    System.out.println(data);
                    String id = (String) data.get("id");
                    int productId = Integer.parseInt(id);
                    String productName = (String) data.get("name");
                    String productPrice = (String) data.get("price");
                    BigDecimal price = new BigDecimal(productPrice);
                    String productUrlImage = (String) data.get("image");
                    String productStatus = (String) data.get("status");
                    boolean isActive = "1".equals(productStatus);
                    System.out.println("id: " + id);
                    System.out.println("productName: " + productName);
                    System.out.println("productPrice: " + price);
                    System.out.println("productUrlImage: " + productUrlImage);
                    System.out.println("productStatus: " + productStatus);
                    System.out.println("isActive: " + isActive);
                    Product product = new Product();
                    product.setProductName(productName);
                    product.setPrice(price);
                    product.setImageUrl(productUrlImage);
                    product.setStatus(isActive);
                    productServiceImp.updateProduct(productId,product);
                    return ResponseEntity.ok(Map.of("message", "Cập nhật sản phẩm thành công"));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                            "error", "Cập nhật sản phẩm thất bại: " + e.getMessage()
                    ));
                }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật sản phẩm thành công"));
    }

    @PostMapping(value = "/admin/blockProduct", consumes = "application/json")
    public ResponseEntity<?> blockProduct(@RequestBody Map<String, Object> data, Model model,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                try {
                    String id = (String) data.get("id");
                    int productId = Integer.parseInt(id);

//                    String productName = (String) data.get("name");
//                    String productPrice = (String) data.get("price");
//                    BigDecimal price = new BigDecimal(productPrice);
//                    String productUrlImage = (String) data.get("image");
//                    String productStatus = (String) data.get("status");
//                    boolean isActive = "1".equals(productStatus);
//                    System.out.println("id: " + id);
//                    System.out.println("productName: " + productName);
//                    System.out.println("productPrice: " + price);
//                    System.out.println("productUrlImage: " + productUrlImage);
//                    System.out.println("productStatus: " + productStatus);
//                    System.out.println("isActive: " + isActive);
                    Product product = new Product();
                    Optional<Product> pd= productServiceImp.getProductById(productId);
                    product.setProductName(pd.get().getProductName());
                    product.setPrice(pd.get().getPrice());
                    product.setImageUrl(pd.get().getImageUrl());
                    product.setStatus(false);
                    productServiceImp.updateProduct(productId,product);
                    return ResponseEntity.ok(Map.of("message", "Khóa sản phẩm thành công"));
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                            "error", "Khóa sản phẩm thất bại: " + e.getMessage()
                    ));
                }
            }
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Khóa sản phẩm thành công"));
    }

    // Lấy thông tin đơn hàng theo ID
    @GetMapping("/admin/orders/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int id) {
        OrderDTO order = orderServiceImp.findById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Lấy danh sách sản phẩm trong đơn hàng
    @GetMapping("/admin/orders/{id}/details")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetails(@PathVariable int id) {
        Pageable pageable = PageRequest.of(0, 10);

        var orderDetails = orderdetailServiceImp.findByOrderId(id, pageable);

        if (orderDetails != null && orderDetails.hasContent()) {
            return ResponseEntity.ok(orderDetails.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/admin/orders/{orderId}/approve")
    public ResponseEntity<String> approveOrder(@PathVariable int orderId) {
        OrderDTO order = orderServiceImp.findById(orderId);
        if (order != null) {
            // Chỉ cho phép duyệt nếu trạng thái chưa là COMPLETED hoặc CANCELLED
            if (!order.getOrderStatus().equals("COMPLETED") && !order.getOrderStatus().equals("CANCELLED")) {
                order.setOrderStatus(OrderStatus.valueOf("COMPLETED"));
                Order od = new Order();
                od.setOrderStatus(OrderStatus.valueOf("COMPLETED"));
                orderServiceImp.updateOrder(orderId,od);
                return ResponseEntity.ok("Đơn hàng đã được duyệt thành công");
            } else {
                return ResponseEntity.badRequest().body("Không duyệt được");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/admin/orders/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId) {
        OrderDTO order = orderServiceImp.findById(orderId);
        if (order != null) {
            // Chỉ cho phép hủy nếu trạng thái chưa là CANCELLED hoặc COMPLETED
            if (!order.getOrderStatus().equals("CANCELLED") && !order.getOrderStatus().equals("COMPLETED")) {
                order.setOrderStatus(OrderStatus.valueOf("CANCELLED"));
                Order od = new Order();
                od.setOrderStatus(OrderStatus.valueOf("CANCELLED"));
                orderServiceImp.updateOrder(orderId, od);
                return ResponseEntity.ok("Đơn hàng đã được hủy thành công");
            } else {
                return ResponseEntity.badRequest().body("Không thể hủy đơn hàng trong trạng thái này");
            }
        }
        return ResponseEntity.notFound().build();
    }
}
