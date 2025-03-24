package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.ProductDTO;
import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Repository.ProductRepository;
import com.example.vouchermanager.Service.ProductServiceImp;
import com.example.vouchermanager.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class StoreController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductServiceImp productServiceImp;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/store")
    public String checkLoginStorePage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);

        List<ProductDTO> products = productServiceImp.findAll();
        model.addAttribute("products", products);
        return "user/store";
    }

    @PostMapping(value = "/store", consumes = "application/json")
    public String printCart(@RequestBody String requestData, Model model, HttpSession session) {
        System.out.println("Dữ liệu JSON nhận được: " + requestData);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String fixedJson = objectMapper.readValue(requestData, String.class);
            List<Map<String, Object>> cartItems = objectMapper.readValue(fixedJson, new TypeReference<>() {});
            List<Integer> ids = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for (Map<String, Object> item : cartItems) {
                ids.add((Integer) item.get("id"));
                quantities.add((Integer) item.get("quantity"));
            }

            System.out.println("Danh sách ID: " + ids);
            System.out.println("Danh sách Quantity: " + quantities);
            List<Long> idsLong = ids.stream()
                    .map(Integer::longValue)
                    .collect(Collectors.toList());

            List<Product> cart = productRepository.findAllById(idsLong);
            session.setAttribute("cart", cart);
            model.addAttribute("cart", cart);

            return "redirect:/store";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "error";
        }
    }

}
