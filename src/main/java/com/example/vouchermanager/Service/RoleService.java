package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.RoleDTO;
import com.example.vouchermanager.Model.Entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoleService {
//    List<Role> findAll();
    Optional<Role> findById(int id);
//    Role findByRoleName(String name);
    Page<RoleDTO> findByRoleName(String roleName, Pageable pageable);
    Page<RoleDTO> findAll(Pageable pageable);
}

