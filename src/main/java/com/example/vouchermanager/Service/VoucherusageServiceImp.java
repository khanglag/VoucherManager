package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.DailyVoucherUsageDTO;
import com.example.vouchermanager.Model.DTO.VoucherUsageDTO;
import com.example.vouchermanager.Repository.VoucherusageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoucherusageServiceImp implements VoucherusageService {
    @Autowired
    private VoucherusageRepository voucherusageRepository;

    @Override
    public List<VoucherUsageDTO> findAll() {
        return voucherusageRepository.findAll().stream()
                .map(voucherusage -> new VoucherUsageDTO(
                        voucherusage.getId(),
                        voucherusage.getOrderID().getId(),
                        voucherusage.getVoucherCode().getVoucherCode(),
                        convertInstantToLocalDateTime(voucherusage.getUsedDate())
                ))
                .collect(Collectors.toList());
    }

    private LocalDateTime convertInstantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }
    public List<DailyVoucherUsageDTO> getVoucherUsage(LocalDate startDate, LocalDate endDate) {
        // Chuyển LocalDate thành Instant (với giờ đầu và cuối ngày)
        Instant startOfDay = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        // Lấy dữ liệu từ DB
        List<Object[]> results = voucherusageRepository.findVoucherUsage(startOfDay, endOfDay);

        // Xử lý và gom nhóm theo ngày
        Map<LocalDate, Long> usageMap = new HashMap<>();
        for (Object[] result : results) {
            Instant usedInstant = (Instant) result[0];
            LocalDate usedDate = usedInstant.atZone(ZoneId.systemDefault()).toLocalDate();
            Long count = (Long) result[1];

            usageMap.put(usedDate, usageMap.getOrDefault(usedDate, 0L) + count);
        }

        // Chuyển Map thành danh sách DTO
        return usageMap.entrySet().stream()
                .map(entry -> new DailyVoucherUsageDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DailyVoucherUsageDTO::getDate))
                .collect(Collectors.toList());
    }


}
