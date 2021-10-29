package ru.oeru.SpringBoot.servise;

import ru.oeru.SpringBoot.model.Role;

public interface RoleService {
    Role findByName(String name);
    void createRoleIfNotExist(Role role);
}
