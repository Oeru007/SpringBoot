package ru.oeru.SpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.oeru.SpringBoot.model.User;
import ru.oeru.SpringBoot.servise.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (userService.add(user)) {
            return "redirect:login";
        } else {
            model.addAttribute("userIsExist", "This user already exists");
            return "registration";
        }
    }

    @GetMapping("/userdetails")
    public String userdetailsPage(Model model, Principal principal){
        model.addAttribute(userService.findUserByUsername(principal.getName()));
        return "userdetails";
    }


}
