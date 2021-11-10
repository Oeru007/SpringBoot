package ru.oeru.SpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.oeru.SpringBoot.utils.UserUtils;
import ru.oeru.SpringBoot.model.User;
import ru.oeru.SpringBoot.servise.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String usersPage(Model model, Principal principal){
        List<User> users = userService.listUsers();
        User user = userService.findUserByUsername(principal.getName());
        users = users.stream().peek(user1 -> user1.setPassword("")).collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "users";
    }
    @PostMapping
    public String createUser(@ModelAttribute("newUser") User user, Model model){
        UserUtils.formValidation(user, model);
        userService.add(user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("userFromDB") User user, @PathVariable("id") Long id, Model model) {
        UserUtils.formValidation(user, model);
        user.setId(id);
        userService.update(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.removeById(id);
        return "redirect:/admin";
    }
}
