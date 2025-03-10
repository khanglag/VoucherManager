package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDTO> findAll();
}
