package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherCreationResultDTO;
import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Model.DTO.VoucherDeactivationResultDTO;
import com.example.vouchermanager.Model.DTO.VoucherUsageResultDTO;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import com.example.vouchermanager.Repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImp implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

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
        return vouchers .map(voucher -> new VoucherDTO(
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
            LocalDate endDate, java.awt.print.Pageable pageable) {
        return null;
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
            return new VoucherCreationResultDTO(savedVoucher, null, "Voucher đã được tạo thành công!");
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
}
