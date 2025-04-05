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
//    @Test
//    public void testFindAll() {
//        List<User> users = userServiceImp.findAll();
////        users.forEach(System.out::println);
//        for(User user : users){
//            System.out.println(user.getRoleID().getRoleName());
//        }
//    }
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

    @Test
    public void testUpdateUser(){
<<<<<<< HEAD
        User user = new User();
        user.setFullName("Nguyen Ke Cuong");

        int id = 2;

        userServiceImp.updateUser(id, user);
=======

        userServiceImp.getAllUsersByRoleId(2);
>>>>>>> main
    }

    @Test
    public void testFindUserById(){
        User user = userServiceImp.findById(1);
        System.out.println(user.toString());
    }
    @Test
    public void testChangePassword(){
        System.out.println(userServiceImp.changPassword(7,"123456","123456"));

    }
    @Test
    public void testFindByEmail(){
        User user = userServiceImp.findByEmail("admin@gmail.com");
        System.out.println(user.getRoleID().getId());
    }
    @Test
    public void testFindByPhoneNumber(){
        System.out.println(userServiceImp.findByPhoneNumber("0987654321"));
    }
//    @Test
//    public void testFindByRoleID(){
//        Role role = new Role();
//        role.setId(1);
//        List<User> users = userServiceImp.findByRoleID(role);
//        users.forEach(System.out::println);
//    }
}
