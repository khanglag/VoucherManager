package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.ProductDTO;
import com.example.vouchermanager.Model.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<ProductDTO> findAll();

    List<Product> getAllProducts();

    Optional<Product> getProductById(Integer id);

    Product createProduct(Product product);

    Optional<Product> updateProduct(Integer id, Product productDetails);

    boolean deleteProduct(Integer id);

    Page<Product> getProducts(int page, int size, String sortBy, String sortDirection, String productName, BigDecimal minPrice, BigDecimal maxPrice, Boolean status);
}
