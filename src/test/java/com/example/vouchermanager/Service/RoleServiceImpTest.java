package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.RoleDTO;
import com.example.vouchermanager.Model.Entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RoleServiceImpTest {
    @Autowired
    RoleServiceImp roleServiceImp;

//    @Test
//    public void testFindAll() {
//        List<Role> roles = roleServiceImp.findAll();
//        roles.forEach(System.out::println);
//    }

    @Test
    public void testFindById() {
        Optional<Role> role = roleServiceImp.findById(1);
        System.out.println(role.get());
    }

//    @Test
//    public void testFindByRoleName() {
//        Role role = roleServiceImp.findByRoleName("admin");
//        System.out.println(role);
//    }

    @Test
    public void testCreate() {
        Role role = new Role();
        role.setRoleName("Viewer");
        Role newRole = roleServiceImp.createRole(role);
        System.out.println(newRole);

    }

    @Test
    public void testUpdate() {
        Role role = new Role();
        role.setRoleName("Viewer1");
        int id = 3;
        Role newRole = roleServiceImp.updateRole(id, role);
        System.out.println(newRole);

    }

}
