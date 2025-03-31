//package com.example.vouchermanager.Service;
//
//import com.example.vouchermanager.Model.Entity.Product;
//import com.example.vouchermanager.Repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProducTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private ProductServiceImp productService;
//
//
//    private Product existingProduct;
//
//    @BeforeEach
//    void setUp() {
//        existingProduct = new Product();
//        existingProduct.setId(1);
//        existingProduct.setProductName("Old Product");
//        existingProduct.setPrice(new BigDecimal("100.00"));
//        existingProduct.setImageUrl("old_image.jpg");
//        existingProduct.setStatus(true);
//    }
//
//    @Test
//    void testUpdateProduct_Success() {
//        // Giả lập dữ liệu
//        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
//
//        // Dữ liệu mới cập nhật
//        Product productDetails = new Product();
//        productDetails.setProductName("New Product");
//        productDetails.setPrice(new BigDecimal("150.00"));
//
//        // Gọi phương thức update
//        Optional<Product> updatedProductOpt = productService.updateProduct(1, productDetails);
//
//        // Kiểm tra kết quả
//        assertTrue(updatedProductOpt.isPresent());
//        Product updatedProduct = updatedProductOpt.get();
//        assertEquals("New Product", updatedProduct.getProductName());
//        assertEquals(new BigDecimal("150.00"), updatedProduct.getPrice());
//        assertEquals("old_image.jpg", updatedProduct.getImageUrl()); // Không thay đổi
//        assertTrue(updatedProduct.getStatus());
//
//        // Kiểm tra productRepository.save() có được gọi không
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    void testUpdateProduct_NotFound() {
//        when(productRepository.findById(99L)).thenReturn(Optional.empty());
//
//        Product productDetails = new Product();
//        productDetails.setProductName("New Product");
//
//        Optional<Product> updatedProduct = productService.updateProduct(99, productDetails);
//
//        assertFalse(updatedProduct.isPresent());
//        verify(productRepository, never()).save(any(Product.class));
//    }
//
//    @Test
//    void testUpdateProduct_NullFields_NotUpdated() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
//
//        // DTO không có giá trị mới (tất cả đều null)
//        Product productDetails = new Product();
//
//        Optional<Product> updatedProductOpt = productService.updateProduct(1, productDetails);
//
//        assertTrue(updatedProductOpt.isPresent());
//        Product updatedProduct = updatedProductOpt.get();
//
//        // Kiểm tra dữ liệu không bị thay đổi
//        assertEquals("Old Product", updatedProduct.getProductName());
//        assertEquals(new BigDecimal("100.00"), updatedProduct.getPrice());
//        assertEquals("old_image.jpg", updatedProduct.getImageUrl());
//        assertTrue(updatedProduct.getStatus());
//
//        verify(productRepository, times(1)).save(existingProduct);
//    }
//}