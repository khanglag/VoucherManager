//package com.example.vouchermanager.Service;
//
//import com.example.vouchermanager.Model.DTO.PurchaseRequestDTO;
//import com.example.vouchermanager.Model.Entity.Orderdetail;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//public class PurchaseTest {
//    @Autowired
//    private PurchaseServiceImp purchaseService;
//    @Autowired
//    private ProductService productService;
//
//
//    @Test
//    public void Mua(){
//        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
//        purchaseRequestDTO.setUserId(1L);
//        Orderdetail orderdetail = new Orderdetail();
//        orderdetail.setQuantity(2);
//        orderdetail.setUnitPrice(BigDecimal.valueOf(800.00));
//        orderdetail.setTotalPrice(BigDecimal.valueOf(1600.00));
//        orderdetail.setProductID(productService.getProductById(2).stream().findFirst().get());
//        List<Orderdetail> list = new ArrayList<>();
//        list.add(orderdetail);
//        purchaseRequestDTO.setOrderdetails(list);
//        List<String> list1 = new ArrayList<>();
//        list1.add("SHIP001");
//        list1.add("FIXED20");
//        purchaseRequestDTO.setVoucherCodes(list1);
//        System.out.println(purchaseRequestDTO);
//        System.out.println(purchaseService.processPurchase(purchaseRequestDTO));
//    }
//
//}
