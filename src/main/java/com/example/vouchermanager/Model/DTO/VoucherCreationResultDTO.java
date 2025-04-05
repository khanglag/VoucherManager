package com.example.vouchermanager.Model.DTO;

import com.example.vouchermanager.Model.Entity.Voucher;

import java.util.List;

public class VoucherCreationResultDTO {
    private Voucher voucher;
    private List<String> suggestions;
    private String message;

    public VoucherCreationResultDTO(Voucher voucher, List<String> suggestions, String message) {
        this.voucher = voucher;
        this.suggestions = suggestions;
        this.message = message;
    }

    public Voucher getVoucher() { return voucher; }
    public List<String> getSuggestions() { return suggestions; }
    public String getMessage() { return message; }
}
