package com.katia.spring.security.controllers;

import com.katia.spring.security.entities.Role;
import com.katia.spring.security.model.CreateOrEditUserModel;
import com.katia.spring.security.entities.User;
import com.katia.spring.security.repositories.UserRepository;
import com.katia.spring.security.services.RoleService;
import com.katia.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MainController (UserService userService, UserRepository userRepository, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String getLogoutPage() {
        return "login";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {

        model.addAttribute("user", userService.findById(id));
        return "show";
    }

    @GetMapping("/admin/users")
    public String findAll(Model model){

        model.addAttribute("users", userService.findAll());

        return "users";
    }

// ---------------------------------UPDATE-------------------------------------------
    @GetMapping("/admin/{id}/edit")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {

        model.addAttribute("userUpdateDto", userService.findById(id));

        if (model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", model.getAttribute("errorMessage"));
        }
        return "edit";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@ModelAttribute("userUpdateDto") CreateOrEditUserModel userUpdateDto,
                             @PathVariable("id") Long id,
                             @RequestParam(value = "roleNames", required = true) String[] roleNames,
                             RedirectAttributes redirectAttributes) {

        String email = userUpdateDto.getEmail();
        if (email !=null && userService.isEmailTakenByOtherUser(email, id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "User with email: " + email + " already exists. Try again.");
            redirectAttributes.addAttribute("id", id);
            return "edit";
        } else {
            userUpdateDto.setRoleNames(Arrays.asList(roleNames));
            userService.updateUser(id, userUpdateDto);
            return "redirect:/admin/users";
        }
    }

// ---------------------------------CREATE-------------------------------------------

    @GetMapping("/admin/new")
    public String createUser(Model model) {
        model.addAttribute("userCreateDto", new CreateOrEditUserModel());

        if (model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", model.getAttribute("errorMessage"));
        }
        return "new";
    }
    @PostMapping("/admin/users")
    public String createUser(@ModelAttribute("userCreateDto") CreateOrEditUserModel userCreateDto,
                             @RequestParam(value = "roleNames", required = true) String[] roleNames,
                             RedirectAttributes redirectAttributes) {

        String email = userCreateDto.getEmail();
        if (userService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "User with email: " + email + " already exists.");
            return "redirect:/admin/new";
        } else {
            userCreateDto.setRoleNames(Arrays.asList(roleNames));
            userService.saveUser(userCreateDto);
            return "redirect:/admin/users";
        }
    }
// ---------------------------------DELETE-------------------------------------------
    @PostMapping("/admin/users/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
