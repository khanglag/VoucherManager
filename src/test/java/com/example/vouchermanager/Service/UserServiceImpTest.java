package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.UserDTO;
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
}
