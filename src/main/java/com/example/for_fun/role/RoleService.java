package com.example.for_fun.role;

import com.example.for_fun.role.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity getDefaultRole() throws RoleNotFoundException {
        return this.roleRepository.getByName("user").orElseThrow(RoleNotFoundException::new);
    }

}
