package com.example.sweater.controllers;


import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class LikesController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("like/{message}")
    public String likeFromUser(@AuthenticationPrincipal User user,
                               @PathVariable Message message,
                               RedirectAttributes redirectAttributes,
                               @RequestHeader(required = false) String referer) {
        user = userRepo.findByUsername(user.getUsername());
        if (!message.getLikesFrom().contains(user)) {
            message.getLikesFrom().add(user);
        } else {
            message.getLikesFrom().remove(user);
        }
        messageRepo.save(message);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }

    @GetMapping("/like/{message}/likesfrom/")
    public String likesFrom(Model model,
                            @PathVariable Message message) {
        model.addAttribute("users", message.getLikesFrom());
        return "likeslist";
    }
}
