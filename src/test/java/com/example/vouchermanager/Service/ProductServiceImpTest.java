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
        product.setProductName("Nước hoa nữ Gucci");
        product.setImageUrl("https://product.hstatic.net/200000791527/product/screenshot_1841_80e6d2e71ac84744a6b7d1ff9ecc96de_grande.png");
        product.setPrice(BigDecimal.valueOf(11990000));
        product.setStatus(true);
        productServiceImp.createProduct(product);
    }
}
