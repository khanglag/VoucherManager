package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.ProductDTO;
import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getImageUrl(),
                        product.getStatus()
                ))
                .collect(Collectors.toList());
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(Long.valueOf(id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Integer id, Product productDetails) {
        return productRepository.findById(Long.valueOf(id)).map(product -> {
            product.setProductName(productDetails.getProductName());
            product.setPrice(productDetails.getPrice());
            product.setImageUrl(productDetails.getImageUrl());
            product.setStatus(productDetails.getStatus());
            return productRepository.save(product);
        });
    }

    public boolean deleteProduct(Integer id) {
        return productRepository.findById(Long.valueOf(id)).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }
    public Page<Product> getProducts(int page, int size, String sortBy, String sortDirection, String productName, BigDecimal minPrice, BigDecimal maxPrice, Boolean status) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
        Specification<Product> spec = ProductSpecification.filterProducts(productName, minPrice, maxPrice, status);
        return productRepository.findAll(spec, (org.springframework.data.domain.Pageable) pageable);
    }
}
