package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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

    @GetMapping("/updatePassword")
    public String updatePas(){
        return "updatePassword";
    }

    @PostMapping("/user/registration")
    public String createUser(User user,
                             @RequestParam String password,
                             Model model) {
        String errorMessage = "";

        try {

            if ( (password.length() < 8)||(password.length() > 30) ){
                errorMessage = "This password must be 8-30 symbols";
                model.addAttribute("errorMessage", errorMessage);
                return "registration";
            }
        } catch (DateTimeParseException e) {
            errorMessage = "Invalid date and time format.";
            model.addAttribute("errorMessage", errorMessage);
            return "registration";
        }
        if (!userService.createUser(user)) {
            errorMessage = "User with this email already exists";
            model.addAttribute("errorMessage", errorMessage);
            return "registration";
        }
        return "redirect:/user/login";


    }



}
