package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.Entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Page<Role> findByRoleName(String name, Pageable pageable);
}
