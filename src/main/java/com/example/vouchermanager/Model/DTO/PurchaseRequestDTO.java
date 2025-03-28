package com.example.vouchermanager.Model.DTO;

import com.example.vouchermanager.Model.Entity.Orderdetail;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PurchaseRequestDTO {
    private Long userId; // Chỉ cần ID thay vì toàn bộ User entity
    private List<Orderdetail> orderdetails;
    private List<String> voucherCodes;

}
