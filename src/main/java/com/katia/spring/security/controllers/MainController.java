package com.katia.spring.security.controllers;

import com.katia.spring.security.entities.User;
import com.katia.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class MainController {
    private final UserService userService;
    @Autowired
    public MainController (UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users")
    public String findAll(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "show";
    }@GetMapping("/new")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }
    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") User user, Model model){
        User user1 = userService.saveUser(user);
        model.addAttribute("user", user1);
        return "redirect:/users";
    }
    @GetMapping("/{id}/edit")
    public String updateUserForm(Model model, @PathVariable("id") Long id) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "edit";
    }
    @PostMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userService.updateUser(id, user);
        return "redirect:/users";
    }
    @PostMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "redirect:/users";
    }


}
