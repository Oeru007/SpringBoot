package ru.oeru.SpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.oeru.SpringBoot.configurations.utils.UserUtils;
import ru.oeru.SpringBoot.model.PossibleRoles;
import ru.oeru.SpringBoot.model.User;
import ru.oeru.SpringBoot.servise.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String usersPage(Model model){
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "users";
    }
    @PostMapping
    public String createUser(@ModelAttribute("user") User user, Model model){
        UserUtils.formValidation(user, model);
        if (model.containsAttribute("formError")){
            return "new";
        } else if (userService.add(user)){
            return "redirect:/admin";
        } else {
            model.addAttribute("userIsExist", "This user already exists");
            return "new";
        }
    }

    @GetMapping("/new")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        return "new";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model){
        User user = userService.find(id);
        if (user.getRoles().stream().anyMatch(role -> PossibleRoles.getAdminRole().equals(role.getName()))){
            user.setConfirm("on");
        }
        model.addAttribute("user", user);
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id, Model model) {
        UserUtils.formValidation(user, model);
        user.setId(id);
        if (model.containsAttribute("formError")){
            return "edit";
        }
        userService.update(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.removeById(id);
        return "redirect:/admin";
    }
}
