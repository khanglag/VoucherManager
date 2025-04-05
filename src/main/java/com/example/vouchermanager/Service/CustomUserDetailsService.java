package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.Entity.User;
import com.example.vouchermanager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) { this.userRepository = userRepository; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
<<<<<<< HEAD
            throw new UsernameNotFoundException("User not found!");
=======
            user = userRepository.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found!");
            }
>>>>>>> main
        }
        if (!user.getStatus()) {
            throw new DisabledException("Tài khoản đã bị khóa!");
        }
<<<<<<< HEAD
=======
        System.out.println("===================");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getRoleID().getRoleName());
>>>>>>> main
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoleID().getRoleName())
                .build();
    }
}
