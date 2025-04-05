package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import com.example.vouchermanager.Service.OrderdetailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-details")
=======
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api/order-details")
@Controller
@RequestMapping("/order-details")
>>>>>>> main
public class OrderDetailController {
    @Autowired
    private OrderdetailServiceImp orderdetailService;

    public OrderDetailController(OrderdetailServiceImp orderdetailService) {
        this.orderdetailService = orderdetailService;
    }

    @GetMapping("/all")
    public Page<OrderDetailDTO> getAllOrderDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderdetailService.findAll(pageable);
    }

<<<<<<< HEAD
=======
    @GetMapping("/show-all")
    public String showOrderDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDetailDTO> orderDetails = orderdetailService.findAll(pageable);

        System.out.println("Order Details: " + orderDetails.getContent()); // Kiểm tra log

        model.addAttribute("orderDetails", orderDetails.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderDetails.getTotalPages());

        return "order-details";
    }

>>>>>>> main
    @GetMapping("/order/{orderId}")
    public Page<OrderDetailDTO> getOrderDetailsByOrderId(
            @PathVariable int orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderdetailService.findByOrderId(orderId, pageable);
    }

    @GetMapping("/product/{productId}")
    public Page<OrderDetailDTO> getOrderDetailsByProductId(
            @PathVariable int productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderdetailService.findByProductId(productId, pageable);
    }
}
