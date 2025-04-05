package com.example.vouchermanager.Model.DTO;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    private int productId;
    private String productName;
    private BigDecimal price;
<<<<<<< HEAD
=======
    private String imageUrl;
>>>>>>> main
    private boolean status;

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
<<<<<<< HEAD
=======
                ", imageUrl=" + imageUrl +
>>>>>>> main
                ", status=" + status +
                '}';
    }

}
