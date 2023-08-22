package com.socialmediaapi.service.impl;


import com.socialmediaapi.model.Role;
import com.socialmediaapi.repository.RoleRepository;
import com.socialmediaapi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private final RoleRepository roleRepository;

    /**
     * Метод позвоялет получить роль пользователя
     * при его регистрации/авторизации.
     *
     * @return Role.
     */
    @Override
    public Role getUserRole() {
        Role role = roleRepository.findByName("USER_ROLE").orElse(null);
        if (role == null) {
            createNewRole("USER_ROLE");
        }
        return role;
    }

    /**
     * Вспомогательный метод используемый в getUserRole.
     * Создает новую роль и сохраняет ее.
     *
     * @param name имя роли.
     * @return Role.
     */
    private Role createNewRole(String name) {
        Role role = new Role(name);
        return roleRepository.save(role);
    }
}
