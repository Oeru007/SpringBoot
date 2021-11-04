package ru.oeru.SpringBoot.utils;

import org.springframework.ui.Model;
import ru.oeru.SpringBoot.model.User;

public class UserUtils {
    public static void formValidation(User user, Model model){
        if (user == null){
            return;
        }
        if (user.getUsername().equals("")
                || user.getPassword().equals("")
                || user.getPasswordConfirm().equals("")
                || !user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("formError", "Form filling error");
        }
        if (user.getConfirm() != null && user.getConfirm().equalsIgnoreCase("")) {
            user.setConfirm(null);
        }
    }
}