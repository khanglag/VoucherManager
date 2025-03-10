package com.example.vouchermanager.Model.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoucherApplicableProductDTO {
    private String voucherCode;
    private int productId;

    @Override
    public String toString() {
        return "VoucherApplicableProductDTO {" + "voucherCode=" + voucherCode + ", productId=" + productId + '}';
    }
}
