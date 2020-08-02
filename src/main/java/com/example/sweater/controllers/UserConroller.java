package com.example.sweater.controllers;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
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
    @Autowired
    private UserRepo userRepo;
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

    @GetMapping("profile/{userCode}")
    public String getProfile(Model model, @AuthenticationPrincipal User user, @PathVariable String userCode) {
        if (!userCode.equals(user.getId().toString())) {
            return "redirect:/user/profile/" + user.getId().toString();
        }
        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("profile/{user}")
    public String setProfile(Model model,
                             @AuthenticationPrincipal User user,
                             @RequestParam String password,
                             @RequestParam String email) {

        User us = userRepo.findByUsername(user.getUsername());
        System.out.println(us.getId());


        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getEmail());
        System.out.println("New pass: " + password);
        System.out.println("New email" + email);

        userService.updateProfile(user, password, email);
        return "redirect:/user/profile/{user}";

    }

    @GetMapping("messages/{user}")
    public String getMessageList(@AuthenticationPrincipal User currentUser,
                                 @PathVariable User user,
                                 @RequestParam(required = false) Message message,
                                 Model model) {
        if (message != null) {
            model.addAttribute(message);
        }
        model.addAttribute("currUser", user.getId().equals(currentUser.getId()));
        model.addAttribute("messages", user.getMessages());
        return "usermessages";
    }
}
