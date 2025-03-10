package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<ProductDTO> findAll();
}
