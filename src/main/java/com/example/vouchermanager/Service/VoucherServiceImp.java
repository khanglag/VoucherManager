package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.*;
import com.example.vouchermanager.Model.Entity.*;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import com.example.vouchermanager.Repository.ProductRepository;
import com.example.vouchermanager.Repository.VoucherApplicableProductRepository;
import com.example.vouchermanager.Repository.VoucherRepository;
import com.example.vouchermanager.Repository.VoucherusageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImp implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherusageRepository voucherusageRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VoucherApplicableProductRepository voucherApplicableProductRepository;
    @Autowired
    private UserServiceImp userServiceImp;

    @Override
    public List<VoucherDTO> findAll() {
        return voucherRepository.findAll().stream()
                .map(voucher -> new VoucherDTO(
                        voucher.getVoucherCode(),
                        voucher.getTitle(),
                        voucher.getLogoUrl(),
                        voucher.getDescription(),
                        voucher.getDiscountType(),
                        voucher.getDiscountValue(),
                        voucher.getStartDate(),
                        voucher.getEndDate(),
                        voucher.getMinimumOrderValue(),
                        voucher.getStatus(),
                        voucher.getCreatedBy().getId(),
                        voucher.getUsageCount(),
                        voucher.getMaxUsage(),
                        convertInstantToLocalDateTime(voucher.getCreatedDate()),
                        voucher.getApplicableForAllProducts()))
                .collect(Collectors.toList());
    }

    private LocalDateTime convertInstantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }

    public Page<VoucherDTO> getAllVouchers(Pageable pageable) {
        Page<Voucher> vouchers = voucherRepository.findAll(pageable);
        return vouchers.map(voucher -> new VoucherDTO(
                voucher.getVoucherCode(),
                voucher.getTitle(),
                voucher.getLogoUrl(),
                voucher.getDescription(),
                voucher.getDiscountType(),
                voucher.getDiscountValue(),
                voucher.getStartDate(),
                voucher.getEndDate(),
                voucher.getMinimumOrderValue(),
                voucher.getStatus(),
                voucher.getCreatedBy().getId(),
                voucher.getUsageCount(),
                voucher.getMaxUsage(),
                convertInstantToLocalDateTime(voucher.getCreatedDate()),
                voucher.getApplicableForAllProducts()));
    }

    @Override
    public Optional<Voucher> getById(String voucherCode) {
        return voucherRepository.findById(voucherCode);
    }

    @Override
    public Voucher create(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher update(String voucherCode, Voucher voucher) {
        if (voucherRepository.existsById(voucherCode)) {
            return voucherRepository.save(voucher);
        }
        throw new RuntimeException("Voucher not found");
    }

    @Override
    public void delete(String voucherCode) {
        voucherRepository.deleteById(voucherCode);
    }

    @Override
    public Page<VoucherDTO> findAllFiltered(String title, BigDecimal discountValue, String status, LocalDate startDate,
            LocalDate endDate, Pageable pageable) {
        Specification<Voucher> spec = VoucherSpecification.filterVouchers(title, discountValue, status, startDate,
                endDate);

        Page<Voucher> vouchers = voucherRepository.findAll(spec, (org.springframework.data.domain.Pageable) pageable);

        return vouchers.map(voucher -> new VoucherDTO(
                voucher.getVoucherCode(),
                voucher.getTitle(),
                voucher.getLogoUrl(),
                voucher.getDescription(),
                voucher.getDiscountType(),
                voucher.getDiscountValue(),
                voucher.getStartDate(),
                voucher.getEndDate(),
                voucher.getMinimumOrderValue(),
                voucher.getStatus(),
                voucher.getCreatedBy().getId(),
                voucher.getUsageCount(),
                voucher.getMaxUsage(),
                convertInstantToLocalDateTime(voucher.getCreatedDate()),
                voucher.getApplicableForAllProducts()));
    }

    /**
     * Tạo voucher với mã do người dùng nhập, kiểm tra tồn tại và gợi ý mã thay thế
     * nếu cần
     *
     * @param voucher Voucher chứa thông tin do người dùng nhập
     * @return Kết quả bao gồm voucher đã tạo hoặc danh sách gợi ý nếu mã trùng
     */
    @Override
    public VoucherCreationResultDTO createVoucherWithCustomCode(Voucher voucher) {
        String inputCode = voucher.getVoucherCode();

        // Kiểm tra xem mã đã tồn tại chưa
        if (voucherRepository.existsById(inputCode)) {
            // Nếu tồn tại, tạo danh sách gợi ý mã thay thế
            List<String> suggestions = generateVoucherSuggestions(inputCode, 3); // Gợi ý 3 mã
            return new VoucherCreationResultDTO(null, suggestions,
                    "Mã " + inputCode + " đã tồn tại. Dưới đây là các gợi ý thay thế.");
        } else {
            // Nếu không tồn tại, set đầy đủ các thuộc tính và lưu voucher
            Voucher newVoucher = new Voucher();
            newVoucher.setVoucherCode(inputCode);
            newVoucher.setTitle(voucher.getTitle());
            newVoucher.setLogoUrl(voucher.getLogoUrl()); // Có thể null
            newVoucher.setDescription(voucher.getDescription()); // Có thể null
            newVoucher.setDiscountType(voucher.getDiscountType());
            newVoucher.setDiscountValue(voucher.getDiscountValue());
            newVoucher.setStartDate(voucher.getStartDate());
            newVoucher.setEndDate(voucher.getEndDate());
            newVoucher.setMinimumOrderValue(voucher.getMinimumOrderValue()); // Có thể null
            newVoucher.setStatus(VoucherStatus.ACTIVE); // Mặc định ACTIVE
            newVoucher.setCreatedBy(voucher.getCreatedBy()); // Có thể null nếu không có user
            newVoucher.setUsageCount(0); // Khởi tạo số lần sử dụng
            newVoucher.setMaxUsage(voucher.getMaxUsage() != null ? voucher.getMaxUsage() : 100); // Mặc định 100 nếu
                                                                                                 // null
            newVoucher.setCreatedDate(Instant.now()); // Thời gian tạo
            newVoucher.setApplicableForAllProducts(
                    voucher.getApplicableForAllProducts() != null ? voucher.getApplicableForAllProducts() : false); // Mặc
                                                                                                                    // định
                                                                                                                    // false
                                                                                                                    // nếu
                                                                                                                    // null

            Voucher savedVoucher = voucherRepository.save(newVoucher);
            return new VoucherCreationResultDTO(savedVoucher, Collections.singletonList("success"),
                    "Voucher đã được tạo thành công!");
        }
    }

    /**
     * Tạo danh sách gợi ý mã voucher thay thế
     * 
     * @param baseCode            Mã gốc do người dùng nhập
     * @param numberOfSuggestions Số lượng gợi ý cần tạo
     * @return Danh sách mã gợi ý
     */
    private List<String> generateVoucherSuggestions(String baseCode, int numberOfSuggestions) {
        List<String> suggestions = new ArrayList<>();
        Random random = new Random();
        String characters = "0123456789"; // Dùng số để thêm hậu tố

        for (int i = 0; i < numberOfSuggestions; i++) {
            StringBuilder suggestion = new StringBuilder(baseCode);
            suggestion.append("-");
            for (int j = 0; j < 3; j++) {
                suggestion.append(characters.charAt(random.nextInt(characters.length())));
            }

            String newCode = suggestion.toString();
            if (!voucherRepository.existsById(newCode)) {
                suggestions.add(newCode);
            } else {
                i--; // Giảm i để tạo lại gợi ý khác nếu trùng
            }
        }
        return suggestions;
    }

    @Override
    // Hàm sử dụng voucher
    public VoucherUsageResultDTO useVoucher(String voucherCode, BigDecimal orderValue) {
        Voucher voucher = voucherRepository.findById(voucherCode).orElse(null);

        if (voucher == null) {
            return new VoucherUsageResultDTO(false, null, "Mã voucher không tồn tại!");
        }

        // Kiểm tra trạng thái
        if (voucher.getStatus() != VoucherStatus.ACTIVE) {
            return new VoucherUsageResultDTO(false, null, "Voucher không còn hoạt động!");
        }

        // Kiểm tra ngày hết hạn
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isBefore(voucher.getStartDate()) || currentDate.isAfter(voucher.getEndDate())) {
            return new VoucherUsageResultDTO(false, null, "Voucher đã hết hạn hoặc chưa bắt đầu!");
        }

        // Kiểm tra số lần sử dụng
        if (voucher.getUsageCount() >= voucher.getMaxUsage()) {
            return new VoucherUsageResultDTO(false, null, "Voucher đã hết lượt sử dụng!");
        }

        // Kiểm tra giá trị đơn hàng tối thiểu
        if (voucher.getMinimumOrderValue() != null && orderValue.compareTo(voucher.getMinimumOrderValue()) < 0) {
            return new VoucherUsageResultDTO(false, null, "Giá trị đơn hàng không đủ để áp dụng voucher!");
        }

        // Sử dụng voucher thành công
        voucher.setUsageCount(voucher.getUsageCount() + 1);
        if (voucher.getUsageCount() >= voucher.getMaxUsage()) {
            voucher.setStatus(VoucherStatus.EXPIRED); // Đánh dấu hết lượt sử dụng
        }
        voucherRepository.save(voucher);

        return new VoucherUsageResultDTO(true, voucher.getDiscountValue(), "Áp dụng voucher thành công!");
    }

    @Override
    public VoucherDeactivationResultDTO deactivateVoucher(String voucherCode) {
        Voucher voucher = voucherRepository.findById(voucherCode).orElse(null);

        if (voucher == null) {
            return new VoucherDeactivationResultDTO(false, "Mã voucher không tồn tại!");
        }

        if (voucher.getStatus() == VoucherStatus.CANCELLED || voucher.getStatus() == VoucherStatus.EXPIRED) {
            return new VoucherDeactivationResultDTO(false, "Voucher đã ở trạng thái không hoạt động!");
        }

        // Vô hiệu hóa voucher
        voucher.setStatus(VoucherStatus.CANCELLED);
        voucherRepository.save(voucher);
        return new VoucherDeactivationResultDTO(true, "Voucher " + voucherCode + " đã được vô hiệu hóa thành công!");
    }

    public Integer getTotalMaxUsage() {
        return voucherRepository.getTotalMaxUsage();
    }

    public Integer getRemainingUsageForActiveVouchers() {
        return voucherRepository.getRemainingUsageForActiveVouchers(LocalDate.now());
    }

    public Integer getRemainingUsageForUpcomingVouchers() {
        return voucherRepository.getRemainingUsageForUpcomingVouchers(LocalDate.now());
    }

    public Integer getRemainingUsageForExpiredVouchers() {
        return voucherRepository.getRemainingUsageForExpiredVouchers(LocalDate.now());
    }

    public Integer getTotalCancelledVoucherUsage() {
        return voucherRepository.getTotalCancelledVoucherUsage();
    }

    public Integer getTotalUsedVouchers() {
        return voucherRepository.getTotalUsedVouchers();
    }

    @Override
    public VoucherActivationResultDTO activateVoucher(String voucherCode) {
        Voucher voucher = voucherRepository.findById(voucherCode).orElse(null);
        if (voucher == null) {
            return new VoucherActivationResultDTO(false, "Mã voucher không tồn tại!");
        }
        if (voucher.getStatus() == VoucherStatus.ACTIVE) {
            return new VoucherActivationResultDTO(false, "Voucher đã ở trạng thái hoạt động!");
        }
        if (voucher.getStatus() == VoucherStatus.EXPIRED && voucher.getUsageCount() >= voucher.getMaxUsage()) {
            return new VoucherActivationResultDTO(false, "Voucher đã hết lượt sử dụng, không thể kích hoạt lại!");
        }
        voucher.setStatus(VoucherStatus.ACTIVE);
        voucherRepository.save(voucher);
        return new VoucherActivationResultDTO(true, "Voucher " + voucherCode + " đã được kích hoạt lại thành công!");
    }

    @Override
    // Hàm tìm voucher áp dụng cho sản phẩm cụ thể với phân trang
    public Page<VoucherDTO> findByApplicableProducts(String productId, Pageable pageable) {
        List<Voucher> allVouchers = voucherRepository.findAll();

        // Lọc voucher áp dụng cho sản phẩm
        List<VoucherDTO> applicableVouchers = allVouchers.stream()
                .filter(voucher -> voucher.getApplicableForAllProducts()) // Chỉ lấy voucher áp dụng cho tất cả sản phẩm
                .map(voucher -> new VoucherDTO(
                        voucher.getVoucherCode(),
                        voucher.getTitle(),
                        voucher.getLogoUrl(),
                        voucher.getDescription(),
                        voucher.getDiscountType(),
                        voucher.getDiscountValue(),
                        voucher.getStartDate(),
                        voucher.getEndDate(),
                        voucher.getMinimumOrderValue(),
                        voucher.getStatus(),
                        voucher.getCreatedBy().getId(),
                        voucher.getUsageCount(),
                        voucher.getMaxUsage(),
                        convertInstantToLocalDateTime(voucher.getCreatedDate()),
                        voucher.getApplicableForAllProducts()))
                .collect(Collectors.toList());

        // Phân trang thủ công
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), applicableVouchers.size());
        List<VoucherDTO> pagedVouchers = applicableVouchers.subList(start, end);

        return new PageImpl<>(pagedVouchers, pageable, applicableVouchers.size());
    }

    @Override
    public Voucher create(Voucher voucher, MultipartFile logoFile) {
        if (logoFile != null && !logoFile.isEmpty()) {
            String logoUrl = cloudinaryService.uploadFile(logoFile);
            voucher.setLogoUrl(logoUrl);
        }
        return voucherRepository.save(voucher);
    }

    @Override
    public List<Voucher> getPercentageVouchers(Integer productId) {
        return voucherRepository.findPercentageVouchersByProduct(productId);
    }

    @Override
    public List<Voucher> getFixedVouchers(Integer productId) {
        return voucherRepository.findFixedVouchersByProduct(productId);
    }

    @Override
    public List<Voucher> getFreeShipVouchers(Integer productId) {
        return voucherRepository.findFreeShipVouchersByProduct(productId);

    }

    @Override
    public void createVouchersForProducts(Voucher voucher, List<Integer> productIds) {
        voucher.setApplicableForAllProducts(false);
        createVoucherWithCustomCode(voucher);
        for (Integer productId : productIds) {
            Product product = productRepository.findById(Long.valueOf(productId))
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
            VoucherapplicableproductId id = new VoucherapplicableproductId(voucher.getVoucherCode(),
                    Long.valueOf(productId));
            Voucherapplicableproduct voucherapplicableproduct = new Voucherapplicableproduct(id, voucher, product);
            voucherApplicableProductRepository.save(voucherapplicableproduct);
        }
    }

    // Hàm tính giá trị giảm thực tế của voucher
    private BigDecimal calculateDiscountValue(Voucher voucher, BigDecimal productPrice) {
        if (voucher.getDiscountType() == DiscountType.FIXED) {
            return voucher.getDiscountValue(); // Giảm trực tiếp số tiền
        } else if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
            return productPrice.multiply(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100)); // % giảm giá
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<Voucher> getSortedDiscountVouchers(Integer productId, BigDecimal orderTotal) {
        // Lấy danh sách voucher áp dụng cho sản phẩm cụ thể
        List<Voucher> productVouchers = voucherApplicableProductRepository.findVouchersByProductId(productId);

        // Lấy danh sách voucher áp dụng cho tất cả sản phẩm
        List<Voucher> globalVouchers = voucherRepository.findByApplicableForAllProductsTrue();

        // Gộp hai danh sách lại
        List<Voucher> allVouchers = new ArrayList<>();
        allVouchers.addAll(productVouchers);
        allVouchers.addAll(globalVouchers);

        // Lấy giá sản phẩm
        Product product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        BigDecimal productPrice = product.getPrice();

        // Lọc và sắp xếp voucher
        return allVouchers.stream()
                .filter(v -> (v.getDiscountType() == DiscountType.PERCENTAGE
                        || v.getDiscountType() == DiscountType.FIXED) &&
                        v.getStatus() == VoucherStatus.ACTIVE && // Kiểm tra trạng thái ACTIVE
                        !v.getStartDate().isAfter(LocalDate.now()) && // Chưa đến ngày bắt đầu
                        !v.getEndDate().isBefore(LocalDate.now()) && // Chưa quá ngày kết thúc
                        (v.getUsageCount() < v.getMaxUsage()) && // Số lượng sử dụng chưa đạt max
                        (v.getMinimumOrderValue() == null || orderTotal.compareTo(v.getMinimumOrderValue()) >= 0) // Đơn
                                                                                                                  // hàng
                                                                                                                  // đạt
                                                                                                                  // giá
                                                                                                                  // trị
                                                                                                                  // tối
                                                                                                                  // thiểu
                )
                .sorted((v1, v2) -> {
                    BigDecimal discount1 = calculateDiscountValue(v1, productPrice);
                    BigDecimal discount2 = calculateDiscountValue(v2, productPrice);
                    return discount2.compareTo(discount1); // Sắp xếp giảm dần
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Voucher> getSortedFreeShipVouchers(Integer productId, BigDecimal orderTotal) {
        // Lấy danh sách voucher áp dụng cho sản phẩm cụ thể
        List<Voucher> productVouchers = voucherApplicableProductRepository.findVouchersByProductId(productId);

        // Lấy danh sách voucher áp dụng cho tất cả sản phẩm
        List<Voucher> globalVouchers = voucherRepository.findByApplicableForAllProductsTrue();

        // Gộp hai danh sách lại
        List<Voucher> allVouchers = new ArrayList<>();
        allVouchers.addAll(productVouchers);
        allVouchers.addAll(globalVouchers);

        // Lấy giá sản phẩm
        Product product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        BigDecimal productPrice = product.getPrice();

        return allVouchers.stream()
                .filter(v -> v.getDiscountType() == DiscountType.FREESHIP &&
                        v.getStatus() == VoucherStatus.ACTIVE && // Kiểm tra trạng thái ACTIVE
                        !v.getStartDate().isAfter(LocalDate.now()) && // Chưa đến ngày bắt đầu
                        !v.getEndDate().isBefore(LocalDate.now()) && // Chưa quá ngày kết thúc
                        (v.getUsageCount() < v.getMaxUsage()) &&
                        (v.getMinimumOrderValue() == null || orderTotal.compareTo(v.getMinimumOrderValue()) >= 0))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getVoucherStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        List<Object[]> issuedPerMonth = voucherRepository.countVouchersIssuedPerMonth();
        statistics.put("issuedPerMonth", issuedPerMonth);
        return statistics;
    }

    public Map<String, BigDecimal> getVoucherPerformance() {
        Map<String, BigDecimal> voucherPerformance = new HashMap<>();
        List<Object[]> voucherData = voucherRepository.findVoucherUsageData();

        for (Object[] row : voucherData) {
            String voucherCode = (String) row[0];
            Integer usageCount = (Integer) row[1];
            Integer maxUsage = (Integer) row[2];

            // Tính hiệu suất
            BigDecimal performance = BigDecimal.ZERO;
            if (maxUsage != null && maxUsage > 0) {

                performance = new BigDecimal(usageCount != null ? usageCount : 0)
                        .divide(new BigDecimal(maxUsage), 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100));

                // Làm tròn về 2 chữ số sau dấu phẩy
                performance = performance.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            voucherPerformance.put(voucherCode, performance);
        }

        return voucherPerformance; // Trả về bản đồ hiệu suất của từng voucher
    }

    public List<VoucherPerformanceDTO> calculateVoucherPerformanceForAllUsersWithRoleId2() {
        List<User> users = userServiceImp.getAllUsersByRoleId(2);

        List<VoucherPerformanceDTO> performanceDTOs = new ArrayList<>();

        for (User user : users) {
            List<Voucher> vouchers = voucherRepository.findByCreatedBy(user);

            for (Voucher voucher : vouchers) {
                int totalIssued = voucher.getMaxUsage();
                int totalUsed = voucher.getUsageCount();
                int totalRemaining = totalIssued - totalUsed;

                BigDecimal efficiency = new BigDecimal(totalUsed)
                        .divide(new BigDecimal(totalIssued), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100));

                VoucherPerformanceDTO dto = new VoucherPerformanceDTO(
                        voucher.getVoucherCode(),
                        totalIssued,
                        totalUsed,
                        totalRemaining,
                        efficiency,
                        user.getId(),
                        user.getFullName());
                performanceDTOs.add(dto);
            }
        }

        return performanceDTOs;
    }

    public List<VoucherPerformanceDTO> calculateVoucherPerformanceForAllUsersWithRoleId1() {
        List<User> users = userServiceImp.getAllUsersByRoleId(1);

        List<VoucherPerformanceDTO> performanceDTOs = new ArrayList<>();

        for (User user : users) {
            List<Voucher> vouchers = voucherRepository.findByCreatedBy(user);

            for (Voucher voucher : vouchers) {
                int totalIssued = voucher.getMaxUsage();
                int totalUsed = voucher.getUsageCount();
                int totalRemaining = totalIssued - totalUsed;

                BigDecimal efficiency = new BigDecimal(totalUsed)
                        .divide(new BigDecimal(totalIssued), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100));

                VoucherPerformanceDTO dto = new VoucherPerformanceDTO(
                        voucher.getVoucherCode(),
                        totalIssued,
                        totalUsed,
                        totalRemaining,
                        efficiency,
                        user.getId(),
                        user.getFullName());
                performanceDTOs.add(dto);
            }
        }

        return performanceDTOs;
    }

    @Override
    public List<Voucher> getSortedDiscountVouchers(List<Integer> productIds, BigDecimal orderTotal) {
        // Lấy danh sách voucher áp dụng cho từng sản phẩm trong danh sách productIds
        Set<Voucher> productVouchers = new HashSet<>();
        for (Integer productId : productIds) {
            productVouchers.addAll(voucherApplicableProductRepository.findVouchersByProductId(productId));
        }

        // Lấy danh sách voucher áp dụng cho tất cả sản phẩm
        List<Voucher> globalVouchers = voucherRepository.findByApplicableForAllProductsTrue();

        // Gộp tất cả voucher lại
        List<Voucher> allVouchers = new ArrayList<>(productVouchers);
        allVouchers.addAll(globalVouchers);
        // Lọc voucher hợp lệ
        return allVouchers.stream()
                .filter(v -> (v.getDiscountType() == DiscountType.PERCENTAGE
                        || v.getDiscountType() == DiscountType.FIXED) &&
                        v.getStatus() == VoucherStatus.ACTIVE &&
                        !v.getStartDate().isAfter(LocalDate.now()) &&
                        !v.getEndDate().isBefore(LocalDate.now()) &&
                        (v.getUsageCount() < v.getMaxUsage()) &&
                        (v.getMinimumOrderValue() == null || orderTotal.compareTo(v.getMinimumOrderValue()) >= 0))
                .sorted((v1, v2) -> {
                    BigDecimal discount1 = getMaxDiscount(v1, orderTotal);
                    BigDecimal discount2 = getMaxDiscount(v2, orderTotal);
                    return discount2.compareTo(discount1); // Sắp xếp giảm dần
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Voucher> getSortedFreeShipVouchers(List<Integer> productIds, BigDecimal orderTotal) {
        // Lấy danh sách voucher áp dụng cho từng sản phẩm trong danh sách productIds
        Set<Voucher> productVouchers = new HashSet<>();
        for (Integer productId : productIds) {
            productVouchers.addAll(voucherApplicableProductRepository.findVouchersByProductId(productId));
        }

        // Lấy danh sách voucher áp dụng cho tất cả sản phẩm
        List<Voucher> globalVouchers = voucherRepository.findByApplicableForAllProductsTrue();

        // Gộp tất cả voucher lại
        List<Voucher> allVouchers = new ArrayList<>(productVouchers);
        allVouchers.addAll(globalVouchers);

        // Lấy giá sản phẩm theo ID
        Map<Integer, BigDecimal> productPrices = productRepository
                .findAllById(productIds.stream().map(Long::valueOf).toList())
                .stream().collect(Collectors.toMap(p -> p.getId().intValue(), Product::getPrice));

        // Lọc voucher hợp lệ
        return allVouchers.stream()
                .filter(v -> v.getDiscountType() == DiscountType.FREESHIP && // Chỉ lọc voucher Freeship
                        v.getStatus() == VoucherStatus.ACTIVE && // Kiểm tra trạng thái ACTIVE
                        !v.getStartDate().isAfter(LocalDate.now()) && // Chưa đến ngày bắt đầu
                        !v.getEndDate().isBefore(LocalDate.now()) && // Chưa quá ngày kết thúc
                        (v.getUsageCount() < v.getMaxUsage()) && // Số lượng sử dụng chưa đạt max
                        (v.getMinimumOrderValue() == null || orderTotal.compareTo(v.getMinimumOrderValue()) >= 0)) // Đơn
                                                                                                                   // hàng
                                                                                                                   // đạt
                                                                                                                   // giá
                                                                                                                   // trị
                                                                                                                   // tối
                                                                                                                   // thiểu
                .collect(Collectors.toList());
    }

    // Hàm hỗ trợ tính giảm giá cao nhất trong các sản phẩm
    private BigDecimal getMaxDiscount(Voucher voucher, BigDecimal orderTotal) {
        if (voucher.getDiscountType() == DiscountType.FIXED) {
            // Giảm giá cố định: Trừ giá trị cố định từ tổng tiền đơn hàng
            return voucher.getDiscountValue(); // Trả về giá trị giảm cố định từ voucher
        } else if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
            // Giảm giá theo phần trăm: Tính giảm giá từ tổng giá trị đơn hàng
            return orderTotal.multiply(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO; // Nếu không phải loại giảm giá hợp lệ, trả về 0
    }
    @Override
    public List<Voucher> getVoucherShopByVoucherCode(String voucherCode, List<Integer> productIds, BigDecimal orderTotal){
        List<Voucher> temp = new ArrayList<>();
        for(Voucher voucher:  getSortedDiscountVouchers(productIds, orderTotal)){
            if(voucher.getVoucherCode().equals(voucherCode)){
                temp.add(voucher);
            }
        }
        return temp;
    }
    @Override
    public List<Voucher>getVoucherShipByVoucherCode(String voucherCode, List<Integer> productIds, BigDecimal orderTotal){
        List<Voucher> temp = new ArrayList<>();
        for(Voucher voucher:  getSortedFreeShipVouchers(productIds, orderTotal)){
            if(voucher.getVoucherCode().equals(voucherCode) ){
                temp.add(voucher);
            }
        }
        return temp;
    }
}
