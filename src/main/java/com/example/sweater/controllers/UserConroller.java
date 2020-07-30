package com.example.sweater.controllers;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserConroller {
    @Autowired
    private UserService userService;

    //Список юзеров
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userlist";
    }

    //Форма изменения
    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);

        model.addAttribute("spisokRoles", Role.values());
        model.addAttribute("userRoles", user.getRoles());
        return "useredit";
    }

    //Изменить юзера
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{user}")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
            ) {
        userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("profile")
    public String setProfile(Model model,
                             @AuthenticationPrincipal User user,
                             @RequestParam String password,
                             @RequestParam String email) {

        userService.updateProfile(user, password, email);
        return "redirect:/user/profile";
    }
}
