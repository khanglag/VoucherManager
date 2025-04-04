package com.example.vouchermanager.Model.DTO;

import com.example.vouchermanager.Model.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CartDTO {
    private int id;
    private Product product;
    private int quantity;
}
