package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.UserDTO;
import com.example.vouchermanager.Model.Entity.Role;
import com.example.vouchermanager.Model.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
//    List<UserDTO> findAll();
    List<User> findAll();
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhoneNumber(String phone);
    List<User> findByRoleID(Role role);
    User findById(int id);
}
