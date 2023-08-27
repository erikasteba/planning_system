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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
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
    public String createUser(@RequestParam("file") MultipartFile file,
                             User user,
                             @RequestParam String name,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String confirmPassword,
                             Model model) throws IOException {

        //error messages for all "correct input" checks
        String errorMessage = "";
        String errorConfirmMessage = "";
        String errorEmailMessage = "";
        String errorPasMessage = "";
        String errorNameMessage = "";
        String errorFileMessage = "";
        String errorEmptyFileMessage = "";

        //parameter to indicate errors
        boolean mistake = false;

        //check for name correct input
        if ( name.isEmpty() ){
            errorNameMessage = "Name can not be empty";
            model.addAttribute("errorNameMessage", errorNameMessage);
            logger.warn("Failed to register user Error:  Name can not be empty");
            mistake = true;
        }

        //check for password correct input
        if ( (password.length() < 8)||(password.length() > 30) ){
                errorMessage = "This password must be 8-30 symbols";
                model.addAttribute("errorMessage", errorMessage);
                logger.warn("Failed to register user Error:  This password must be 8-30 symbols");
                mistake = true;
        }

        //check for email correct input
        if (!email.contains("@")) {
            errorEmailMessage = "Email should contain @";
            model.addAttribute("errorEmailMessage", errorEmailMessage);
            logger.warn("Failed to register user Error:  Email should contain @");
            mistake = true;
        }

        //check for matching password and confirmation of password
        if (!password.equals(confirmPassword)) {
            errorPasMessage = "Passwords do not match";
            model.addAttribute("errorPasMessage", errorPasMessage);
            logger.warn("Failed to register user Error:  Passwords do not match");
            mistake = true;
        }


        if ((file.getSize() == 0) || (file == null)) {
            errorEmptyFileMessage = "Profile photo is required";
            model.addAttribute("errorEmptyFileMessage", errorEmptyFileMessage);
            logger.warn("Failed to register user Error:  Profile photo is required");
            mistake = true;
        }else{
            String fileName = file.getOriginalFilename();
            String format = fileName.substring(fileName.length()-3,fileName.length() );

            if (!format.equals("jpg") && !format.equals("png")) {
                errorFileMessage = "Image should be .jpg or .png";
                model.addAttribute("errorFileMessage", errorFileMessage);
                logger.warn("Failed to register user Error:  Image should be .jpg or .png");
                mistake = true;
            }
        }
        //if mistakes were found renew page with message errors
        if(mistake){
            return "registration";
        }
        //check for if user with this email already exists
        if (!userService.createUser(user, file)) {
            errorConfirmMessage = "User with this email already exists";
            model.addAttribute("errorConfirmMessage", errorConfirmMessage);
            logger.warn("Failed to register user Error:  User with this email already exists");
            mistake = true;
        }

        //if mistakes were found renew page with message errors
        if(mistake){
            return "registration";
        }

        logger.info("New user registred with name: {} and email {}", name, email);
        return "redirect:/user/login";

    }



}
