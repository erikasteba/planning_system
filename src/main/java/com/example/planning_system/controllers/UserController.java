package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ActivitiesController.class);

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
                             @RequestParam String name,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String confirmPassword,
                             Model model) {
        String errorMessage = "";
        String errorConfirmMessage = "";
        String errorEmailMessage = "";
        String errorPasMessage = "";
        String errorNameMessage = "";
        boolean mistake = false;

        if ( name.isEmpty() ){
            errorNameMessage = "Name can not be empty";
            model.addAttribute("errorNameMessage", errorNameMessage);
            mistake = true;
        }


        if ( (password.length() < 8)||(password.length() > 30) ){
                errorMessage = "This password must be 8-30 symbols";
                model.addAttribute("errorMessage", errorMessage);
                mistake = true;
        }


        if (!email.contains("@")) {
            errorEmailMessage = "Email should contain @";
            model.addAttribute("errorEmailMessage", errorEmailMessage);
            mistake = true;
        }
        if (!password.equals(confirmPassword)) {
            errorPasMessage = "Passwords do not match";
            model.addAttribute("errorPasMessage", errorPasMessage);
            mistake = true;
        }
        if(mistake){
            return "registration";
        }
        if (!userService.createUser(user)) {
            errorConfirmMessage = "User with this email already exists";
            model.addAttribute("errorConfirmMessage", errorConfirmMessage);
            mistake = true;
        }
        if(mistake){
            return "registration";
        }

        logger.info("New user registred with name: {} and email {}", name, email);
        return "redirect:/user/login";

    }



}
