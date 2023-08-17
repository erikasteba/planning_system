package com.example.planning_system.controllers;

import ch.qos.logback.core.model.Model;
import com.example.planning_system.models.User;
import com.example.planning_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/user/login")
    public String login(){
        return "login";
    }
    @GetMapping("/user/registration")
    public String registration(){
       return "registration";
    }

    @PostMapping("/user/registration")
    public String createUser(User user, Model model){
        if (!userService.createUser(user)){
            model.addText("errorMessage");
            return "registration";
        }
        return "redirect:/user/login";
    }
    @GetMapping("/hello")
    public String securityUrl(){
        return "hello";
    }
    @GetMapping("/homepage")
    public String home(){
        return "homepage";
    }

}
