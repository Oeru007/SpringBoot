package ru.oeru.SpringBoot.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.oeru.SpringBoot.model.PossibleRoles;
import ru.oeru.SpringBoot.model.User;
import ru.oeru.SpringBoot.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private RoleService roleService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean add(User user) {
        User userFromDB = findUserByUsername(user.getUsername());
        if (userFromDB != null){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        roleService.createRoleIfNotExist(PossibleRoles.createUserRole());
        user.addRole(roleService.findByName(PossibleRoles.getUserRole()));
        if (user.getConfirm() != null){
            roleService.createRoleIfNotExist(PossibleRoles.createAdminRole());
            user.addRole(roleService.findByName(PossibleRoles.getAdminRole()));
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public void removeById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(User user) {
        User userFromDB = find(user.getId());

        if (user.getConfirm() == null){
            userFromDB.getRoles().remove(PossibleRoles.createAdminRole());

        } else {
            userFromDB.getRoles().add(PossibleRoles.createAdminRole());
        }
        userFromDB.setFirstName(user.getFirstName());
        userFromDB.setLastName(user.getLastName());
        userFromDB.setEmail(user.getEmail());
        userFromDB.setUsername(user.getUsername());
        userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userFromDB);
    }

    @Override
    public User find(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = findUserByUsername(s);
        if (user == null){
            throw new UsernameNotFoundException("user не найден");
        }
        return findUserByUsername(s);
    }
}
