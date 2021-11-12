package ru.oeru.SpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.oeru.SpringBoot.model.User;
import ru.oeru.SpringBoot.servise.UserService;
import ru.oeru.SpringBoot.utils.UserUtils;

import java.util.List;

@RestController
@RequestMapping("/admin/rest")
public class AdminRestController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        User user = userService.find(id);
        user.setPassword("");
        return user;
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        userService.add(user);
        return userService.findUserByUsername(user.getUsername());
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return userService.find(id);
    }

    @DeleteMapping("/{id}")
    public Long deleteUser(@PathVariable("id") Long id) {
        userService.removeById(id);
        return id;
    }
}
