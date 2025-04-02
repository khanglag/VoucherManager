package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.UserDTO;
import com.example.vouchermanager.Model.Entity.Role;
import com.example.vouchermanager.Model.Entity.User;
import com.example.vouchermanager.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public List<UserDTO> findAll() {
//        return userRepository.findAll().stream()
//                .map(user -> new UserDTO(
//                        user.getId(),
//                        user.getUsername(),
//                        user.getPassword(),
//                        user.getFullName(),
//                        user.getEmail(),
//                        user.getPhoneNumber(),
//                        user.getRoleID().getId(),
//                        user.getStatus()
//                ))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }
    @Override
    public Page<UserDTO> findAll(Pageable pageable){
        return userRepository.findAll(pageable)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRoleID().getId(),
                        user.getStatus()
                ));
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhoneNumber(String phone) {
        return userRepository.findByPhoneNumber(phone);
    }

//    @Override
//    public List<User> findByRoleID(Role role) {
//        return userRepository.findByRoleID(role);
//    }
    @Override
    public Page<UserDTO> findByRoleID(Role role, Pageable pageable) {
        return userRepository.findByRoleID(role, pageable)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRoleID().getId(),
                        user.getStatus()
                ));
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User changPassword(int id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id);
        if(passwordEncoder.matches(oldPassword, user.getPassword())) {
            System.out.println("Đúng");
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        }
        return null;
    }
    @Transactional
    public User forgetPassword(int id, String newPassword) {
        User user = userRepository.findById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(int id, User user) {
        User userExists = userRepository.findById(id);
        System.out.println(userExists);
        if(userExists == null) {
            return null;
        }else{
            if(user.getUsername() != null ) {
                userExists.setUsername(user.getUsername());
            }
            if (user.getFullName() != null ) {
                userExists.setFullName(user.getFullName());
            }
            if (user.getPhoneNumber() != null ) {
                userExists.setPhoneNumber(user.getPhoneNumber());
            }
            if (user.getEmail() != null ) {
                userExists.setEmail(user.getEmail());
            }
            if (user.getRoleID() != null ) {
                userExists.setRoleID(user.getRoleID());
            }
            if (user.getStatus() != null) {
                userExists.setStatus(user.getStatus());
            }
        }

        return userRepository.save(userExists);
    }
}
