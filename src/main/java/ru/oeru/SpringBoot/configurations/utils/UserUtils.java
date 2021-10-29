package ru.oeru.SpringBoot.configurations.utils;

import org.springframework.ui.Model;
import ru.oeru.SpringBoot.model.User;

public class UserUtils {
    public static void formValidation(User user, Model model){
        if (user.getUsername().equals("")
                || user.getPassword().equals("")
                || user.getPasswordConfirm().equals("")
                || !user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("formError", "Form filling error");
        }
    }
}