package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Service.CloudinaryService;
import com.example.vouchermanager.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/save")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            product.setImageUrl(imageUrl);
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm được thêm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        return "redirect:/products/new";
    }
}

