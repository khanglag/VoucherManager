package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.RoleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RoleServiceImpTest {
    @Autowired
    RoleServiceImp roleServiceImp;

    @Test
    public void testFindAll() {
        List<RoleDTO> roles = roleServiceImp.findAll();
        roles.forEach(System.out::println);
    }

}
