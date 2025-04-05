package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.DTO.UserDTO;
import com.example.vouchermanager.Model.Entity.Role;
import com.example.vouchermanager.Model.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    @Query("SELECT u from User u WHERE u.username = :username")
//    Optional<User> findByUsername(@Param("username") String username);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhoneNumber(String phone);
<<<<<<< HEAD
//    List<User> findByRoleID(Role role);
    User findById(int id);
    Page<User> findByRoleID(Role role, Pageable pageable);
=======
    List<User> findByRoleID(Role role);
    User findById(int id);
    Page<User> findByRoleID(Role role, Pageable pageable);

>>>>>>> main
}
