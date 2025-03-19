package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.ProductDTO;
import com.example.vouchermanager.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
                        product.getStatus()
                ))
                .collect(Collectors.toList());
    }

}
