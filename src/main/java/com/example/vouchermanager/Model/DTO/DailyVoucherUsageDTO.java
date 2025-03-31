package com.example.vouchermanager.Model.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DailyVoucherUsageDTO {
    private LocalDate date;
    private Long usageCount;
}
