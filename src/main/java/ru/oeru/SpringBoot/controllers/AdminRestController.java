package ru.oeru.SpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.oeru.SpringBoot.model.User;
import ru.oeru.SpringBoot.servise.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<User> userOpt = userService.find(id);
        if (userOpt.isPresent()){
            userOpt.get().setPassword("");
            return userOpt.get();
        }
        return null;
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        userService.add(user);
        return userService.findUserByUsername(user.getUsername());
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        Optional<User> userFromDbOpt = userService.find(id);
        return userFromDbOpt.orElse(null);
    }

    @DeleteMapping("/{id}")
    public Long deleteUser(@PathVariable("id") Long id) {
        userService.removeById(id);
        return id;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldError;
            if (error instanceof FieldError){
                fieldError = ((FieldError) error).getField();
            } else {
                fieldError = error.getObjectName();
            }
            String errorMessage = error.getDefaultMessage();
            errorResponse.put(fieldError, errorMessage);
        });
        return errorResponse;
    }
}
