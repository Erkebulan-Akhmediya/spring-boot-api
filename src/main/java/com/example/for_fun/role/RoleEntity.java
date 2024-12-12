package com.example.for_fun.role;

import com.example.for_fun.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    public RoleEntity(String name) {
        this.name = name;
    }

}
