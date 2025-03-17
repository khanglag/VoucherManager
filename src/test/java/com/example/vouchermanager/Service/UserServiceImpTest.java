package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.UserDTO;
import com.example.vouchermanager.Model.Entity.Role;
import com.example.vouchermanager.Model.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceImpTest {
    @Autowired
    UserServiceImp userServiceImp;
    @Test
    public void testFindAll() {
        List<UserDTO> users = userServiceImp.findAll();
        users.forEach(System.out::println);
    }
    @Test
    public void testFindByUsername(){
        System.out.println(userServiceImp.findByUsername("admin1"));
    }

    @Test
    public void testCreateUser(){
        Role role = new Role();
        role.setId(2);
        User user = new User();
        user.setUsername("abc");
        user.setPassword("abc");
        user.setFullName("abc");
        user.setEmail("abc@gmail.com");
        user.setPhoneNumber("0336065761");
        user.setRoleID(role);
        user.setStatus(true);
        userServiceImp.createUser(user);
    }
}
