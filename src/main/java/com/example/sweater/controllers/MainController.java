package com.example.sweater.controllers;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;

import com.example.sweater.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String GreetingMessage() {
        return "redirect:/main";
    }
    //перенаправляшка
    @GetMapping("/login")
    public String logged() {
        if (
                SecurityContextHolder.getContext().getAuthentication() != null &&
                        SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                        !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)

        ) {

            return "redirect:/main";
        } else {
            return "login";
        }
    }
    //заход на главную
    @GetMapping("/main")
    public String main(Model model, @AuthenticationPrincipal User user) {
        Iterable<Message> messages = messageRepo.findAllByOrderByIdDesc();
        model.addAttribute("nameUser", user.getUsername());
        model.addAttribute("admin", user.getRoles().contains(Role.ADMIN));
        model.addAttribute("messages", messages);
        model.addAttribute("logined", true);
        return "main";
    }
    //добавить сообщение
    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @Valid Message message,
                      BindingResult bindingResult,
                      Model model,
                      @RequestParam(name="file", required = false, defaultValue = "null") MultipartFile file
                      ) throws IOException {


        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return main(model, user);
        } else {
            FileService.uploadAndSaveFile(message, file);
            messageRepo.save(message);
        }
        System.out.println(message.getText());
        return "redirect:/main";
    }




    //поиск сообщений
    @PostMapping("/main/find")
    public String find(Model model, @RequestParam String filter) {
        if (messageRepo.findByTag(filter) != null) {
            model.addAttribute("messages", messageRepo.findByTag(filter));
            return "main";
        } else {
            return "redirect:/main";
        }
    }


}
