package com.example.sweater.controllers;

import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/user/")
public class SubscribeController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;
    @GetMapping("subscribe/{user}")
    public String subscribeFromUser(@PathVariable User user,
                                    @AuthenticationPrincipal User currentUser) {
        currentUser = userRepo.findByUsername(currentUser.getUsername());
        userService.subscribe(currentUser, user);
        return "redirect:/user/messages/{user}";

    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribeFromUser(@PathVariable User user,
                                      @AuthenticationPrincipal User currentUser) {
        currentUser = userRepo.findByUsername(currentUser.getUsername());
        userService.unsubscribe(currentUser, user);
        return "redirect:/user/messages/{user}";

    }
}
