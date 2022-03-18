package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserServiceImpl userService;

//    @Autowired
//    RoleServiceImpl roleService;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public String hello() {
        return "login";
    }

//    @RequestMapping(value = "registration", method = RequestMethod.GET)
//    public String registr(Model model){
//        User user = new User();
//        model.addAttribute(user);
//        return "registration";
//    }
//
//    @PostMapping("registration")
//    public String registrNew(@ModelAttribute("user") User user){
//        userService.addUser(user);
//        return "redirect:/admin";
//    }

    @GetMapping(value = "user")
    public String viewUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("user", user);
        return "admin";

    }

    @PostMapping("/admin")
    public String addUser(User user, String role) {
        userService.addUser(user, role);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam Long id, @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam Long age,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(required = false) String roleList){

        User user1 = userService.getUserById(id);
        user1.setFirstName(firstName);
        user1.setLastName(lastName);
        user1.setAge(age);
        user1.setEmail(email);
        user1.setPassword(password);

        if (roleList != null) {
            if (roleList.equals("ROLE_ADMIN")) {
                user1.getRoles().clear();
                user1.getRoles().add(new Role(1L, "ROLE_ADMIN"));
            } else {
                user1.getRoles().clear();
                user1.getRoles().add(new Role(2L, "ROLE_USER"));
            }
        }
        userService.updateUser(user1);
        return "redirect:/admin";
    }

    @RequestMapping ("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }
}
