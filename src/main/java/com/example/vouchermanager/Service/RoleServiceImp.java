package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.RoleDTO;
import com.example.vouchermanager.Model.Entity.Role;
import com.example.vouchermanager.Repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(int id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findByRoleName(String name) {
        return roleRepository.findByRoleName(name);
    }

    @Transactional
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(int id, Role role) {
        Role roleExist = roleRepository.findById(id).get();
        if(roleExist != null) {
            roleExist.setRoleName(role.getRoleName());
        }else{
            return null;
        }
        return roleRepository.save(roleExist);
    }
}
