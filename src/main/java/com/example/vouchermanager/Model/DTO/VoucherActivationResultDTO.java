package com.example.vouchermanager.Model.DTO;

public class VoucherActivationResultDTO {
    private boolean success;
    private String message;

    public VoucherActivationResultDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
