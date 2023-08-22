package com.socialmediaapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
@Schema(description = "Сущность роль пользователя")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    @Schema(description = "Идентификатор роли пользователя")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Schema(description = "Имя роли")
    @Column(name = "role_name", unique = true, nullable = false)
    private String name;

    @Schema(description = "Список пользователей роли")
    @JsonIgnore
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private Set<User> users;

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(String role) {
        this(null, role);
    }

}
