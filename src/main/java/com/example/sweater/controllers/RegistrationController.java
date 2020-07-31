package com.example.sweater.controllers;

import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.CaptchaResponseDto;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private final static String captchaurl = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String reg() {
        return "registration";
    }


    //зарегестрироваться
    @PostMapping("/registration")
    public String addUser(@RequestParam String password2,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam("g-recaptcha-response") String captchaResponce) {
        String url = String.format(captchaurl, secret, captchaResponce);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (bindingResult.hasErrors()) {
            if (!response.isSuccess()) {
                model.addAttribute("captchaError", "Капчу заполни, придурок!");
            }
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if (user.getPassword() != null && !password2.equals(user.getPassword())) {
            if (!response.isSuccess()) {
                model.addAttribute("captchaError", "Fill captcha!");
            }
            model.addAttribute("passwordError", "Пароли не равны!");
            return "registration";
        }


        if (!userService.addUser(user)) {
            if (!response.isSuccess()) {
                model.addAttribute("captchaError", "Fill captcha!");
            }
            model.addAttribute("usernameError", "ТАКОЕ УЖЕ ЕСТЬ, ДАВАЙ ПО НОВОЙ!");
            return "registration";
        }

        return "redirect:/login";
    }


    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        System.out.println(code);
        boolean isActivating = userService.activateUser(code);
        if (isActivating) {
            model.addAttribute("message", "You are ACTIVATED NOW!");
        } else {
            model.addAttribute("message", "POSOSI");
        }
        return "login";

    }
}
