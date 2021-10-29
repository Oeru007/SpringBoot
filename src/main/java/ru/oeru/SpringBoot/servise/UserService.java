package ru.oeru.SpringBoot.servise;

import ru.oeru.SpringBoot.model.User;

import java.util.List;

public interface UserService {
    boolean add(User user);
    void removeById(Long id);
    void update(User user);
    User find(Long id);
    List<User> listUsers();
    User findUserByUsername(String username);
}
