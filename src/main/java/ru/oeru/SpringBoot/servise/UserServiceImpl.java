package ru.oeru.SpringBoot.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.oeru.SpringBoot.model.PossibleRoles;
import ru.oeru.SpringBoot.model.User;
import ru.oeru.SpringBoot.repositories.UserRepository;

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
        if (user.getConfirm() != null && !user.getConfirm().equals("")){
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
        Optional<User> userFromDBOpt = find(user.getId());
        if (userFromDBOpt.isPresent()){
            User userFromDB = userFromDBOpt.get();
            if (user.getConfirm() == null){
                userFromDB.getRoles().remove(PossibleRoles.createAdminRole());

            } else {
                userFromDB.getRoles().add(roleService.findByName(PossibleRoles.getAdminRole()));
            }
            userFromDB.setFirstName(user.getFirstName());
            userFromDB.setLastName(user.getLastName());
            userFromDB.setAge(user.getAge());
            userFromDB.setEmail(user.getEmail());
            userFromDB.setUsername(user.getUsername());
            userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userFromDB);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null){
            user = userRepository.findByEmail(username);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = findUserByUsername(s);
        if (user == null){
            throw new UsernameNotFoundException("user not found");
        }
        return findUserByUsername(s);
    }
}
