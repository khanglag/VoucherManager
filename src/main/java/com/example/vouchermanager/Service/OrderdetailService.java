package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderdetailService {
//    List<OrderDetailDTO> findAll();
    OrderDetailDTO findById(int id);
<<<<<<< HEAD
//    List<OrderDetailDTO> findByOrderId(int id);
=======
    List<OrderDetailDTO> findByOrderId(int id);
>>>>>>> main
//    List<OrderDetailDTO> findByProductId(int id);
    Page<OrderDetailDTO> findAll(Pageable pageable);
    Page<OrderDetailDTO> findByOrderId(int id, Pageable pageable);
    Page<OrderDetailDTO> findByProductId(int id, Pageable pageable);
}
