package com.example.vouchermanager.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateQuantityRequestDTO
{
    private int itemId;
    private int quantity;

}
