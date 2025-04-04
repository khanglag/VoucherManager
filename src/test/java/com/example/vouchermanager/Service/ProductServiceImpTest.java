package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.ProductDTO;
import com.example.vouchermanager.Model.Entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class ProductServiceImpTest {
    @Autowired
    private ProductServiceImp productServiceImp;
//    @Test
//    public void testFindAll() {
//        List<ProductDTO> products = productServiceImp.findAll();
//        products.forEach(System.out::println);
//    }

    @Test
    public void createProduct() {
        Product product = new Product();
//        product.setProductName("Giày Gucci");
//        product.setImageUrl("https://giaynation.com/wp-content/uploads/2023/08/Gia%CC%80y-Gucci-Tennis-Ebony-sie%CC%82u-ca%CC%82%CC%81p3.jpeg");
        product.setPrice(BigDecimal.valueOf(13990000));
        product.setStatus(false);
        productServiceImp.updateProduct(4,product);
    }
    @Test
    public void updateProduct() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(13990000));
        product.setImageUrl("HEHEEE");
        product.setStatus(false);
        productServiceImp.updateProduct(4,product);
    }
}
