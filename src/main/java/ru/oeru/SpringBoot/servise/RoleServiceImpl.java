package ru.oeru.SpringBoot.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.oeru.SpringBoot.model.Role;
import ru.oeru.SpringBoot.repositories.RoleRepository;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void createRoleIfNotExist(Role role) {
        Role roleFromDb = findByName(role.getName());
        if (roleFromDb == null) {
            roleRepository.save(role);
        }
    }
}
