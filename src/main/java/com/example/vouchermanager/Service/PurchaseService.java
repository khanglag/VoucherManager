package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.PurchaseRequestDTO;
import com.example.vouchermanager.Model.Entity.Order;

public interface PurchaseService {
    Order processPurchase(PurchaseRequestDTO request);
}
