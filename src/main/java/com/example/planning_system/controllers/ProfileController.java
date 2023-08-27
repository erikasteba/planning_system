package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class ProfileController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ActivitiesController.class);


    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);

        //add picture to profile
        String url = "";
        if (currentUser.getImages() == null){
            logger.warn("Failed to upload image for userId: {} and name {}.Error: {} ", currentUser.getId(), currentUser.getName(), "User does not have picture");
            url = "/images/";
        }else {
            url = "/images/" + currentUser.getImages().getId();
        }
        model.addAttribute("img", url);
        return "profile";
    }

    @GetMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        //add picture to profile
        String url = "";
        if (currentUser.getImages() == null){
            logger.warn("Failed to upload image for userId: {} and name {}.Error: {} ", currentUser.getId(), currentUser.getName(), "User does not have picture");
            url = "/images/";
        }else {
            url = "/images/" + currentUser.getImages().getId();
        }
        model.addAttribute("img", url);
        return "edit-profile";
    }

    @PostMapping("/update-username")
    public String updateUsername(@AuthenticationPrincipal User currentUser, String newUsername, RedirectAttributes redirectAttributes) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("updateUsernameMessage", "Username cannot be empty.");
            logger.warn("Failed to update username for userId: {} and name {}. Error: {}", currentUser.getId(), currentUser.getName(), "Username cannot be empty.");
            return "redirect:/user/edit-profile";
        }
        if (userService.existsByUsername(newUsername)) {
            redirectAttributes.addFlashAttribute("updateUsernameMessage", "Username is already taken.");
            logger.warn("Failed to update username for userId: {} and name {}. Error: {}", currentUser.getId(), currentUser.getName(), "Username is already taken.");
            return "redirect:/user/edit-profile";
        }
        String prevUsername = currentUser.getUsername();
        currentUser.setUsername(newUsername);
        userService.updateUser(currentUser);
        logger.info("Updated username for userId: {}. Previous value: {}. New value: {}", currentUser.getId(), prevUsername, newUsername);
        redirectAttributes.addFlashAttribute("updateUsernameMessage", "Username updated successfully.");
        return "redirect:/user/edit-profile";
    }

    @PostMapping("/update-name")
    public String updateName(@AuthenticationPrincipal User currentUser, String newName, RedirectAttributes redirectAttributes) {
        if (newName == null || newName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("updateNameMessage", "Name cannot be empty.");
            return "redirect:/user/edit-profile";
        }
        String prevName = currentUser.getName();
        currentUser.setName(newName);
        userService.updateUser(currentUser);
        logger.info("Updated name for userId: {}. Previous value: {}. New value: {}", currentUser.getId(), prevName, newName);
        redirectAttributes.addFlashAttribute("updateNameMessage", "Name updated successfully.");
        return "redirect:/user/edit-profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal User currentUser, String newEmail, RedirectAttributes redirectAttributes) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("updateEmailMessage", "Email cannot be empty.");
            return "redirect:/user/edit-profile";
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(newEmail);

        if (!matcher.matches()) {
            redirectAttributes.addFlashAttribute("updateEmailMessage", "Invalid email format.");
            return "redirect:/user/edit-profile";
        }
        if (userService.existsByEmail(newEmail)) {
            redirectAttributes.addFlashAttribute("updateEmailMessage", "Email is already in use.");
            return "redirect:/user/edit-profile";
        }
        String prevEmail = currentUser.getName();
        currentUser.setEmail(newEmail);
        userService.updateUser(currentUser);
        logger.info("Updated email for userId: {}. Previous value: {}. New value: {}", currentUser.getId(), prevEmail, newEmail);
        redirectAttributes.addFlashAttribute("updateEmailMessage", "Email updated successfully.");
        return "redirect:/user/edit-profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal User currentUser, String newPassword, String confirmPassword, RedirectAttributes redirectAttributes) {
        if (newPassword == null || newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("updatePasswordMessage", "Password must be at least 8 characters long.");
            return "redirect:/user/edit-profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("updatePasswordMessage", "Password and confirmation do not match.");
            return "redirect:/user/edit-profile";
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        currentUser.setPassword(encodedPassword);
        userService.updateUser(currentUser);
        logger.info("Updated password for userId: {}", currentUser.getId());
        redirectAttributes.addFlashAttribute("updatePasswordMessage", "Password updated successfully.");
        return "redirect:/user/edit-profile";
    }
}

