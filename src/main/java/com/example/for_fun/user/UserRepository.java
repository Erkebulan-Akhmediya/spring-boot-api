package com.example.for_fun.user;

import com.example.for_fun.role.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByRolesContaining(Set<RoleEntity> roles);

    List<UserEntity> findAllByIsActive(boolean isActive);
}
