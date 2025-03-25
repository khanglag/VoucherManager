//package com.example.vouchermanager.Service;
//
//import com.example.vouchermanager.Model.DTO.PurchaseRequestDTO;
//import com.example.vouchermanager.Model.Entity.Orderdetail;
//import com.example.vouchermanager.Model.Entity.Voucher;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//public class PurchaseTest {
//    @Autowired
//    private PurchaseService purchaseService;
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
//        list1.add("SALE11");
//        list1.add("VOUCHER005");
//        purchaseRequestDTO.setVoucherCodes(list1);
//        System.out.println(purchaseRequestDTO);
//        System.out.println(purchaseService.processPurchase(purchaseRequestDTO));
//    }
//
//}
