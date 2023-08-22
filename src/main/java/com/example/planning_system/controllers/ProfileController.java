package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class ProfileController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("/update-username")
    public String updateUsername(@AuthenticationPrincipal User currentUser, String newUsername, RedirectAttributes redirectAttributes) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("updateUsernameMessage", "Username cannot be empty.");
            return "redirect:/user/profile";
        }
        if (userService.existsByUsername(newUsername)) {
            redirectAttributes.addFlashAttribute("updateUsernameMessage", "Username is already taken.");
            return "redirect:/user/profile";
        }
        currentUser.setUsername(newUsername);
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("updateUsernameMessage", "Username updated successfully.");
        return "redirect:/user/profile";
    }
    @PostMapping("/update-name")
    public String updateName(@AuthenticationPrincipal User currentUser, String newName, RedirectAttributes redirectAttributes) {
        if (newName == null || newName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("updateNameMessage", "Name cannot be empty.");
            return "redirect:/user/profile";
        }
        currentUser.setName(newName);
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("updateNameMessage", "Name updated successfully.");
        return "redirect:/user/profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal User currentUser, String newEmail, RedirectAttributes redirectAttributes) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("updateEmailMessage", "Email cannot be empty.");
            return "redirect:/user/profile";
        }
        if (userService.existsByEmail(newEmail)) {
            redirectAttributes.addFlashAttribute("updateEmailMessage", "Email is already in use.");
            return "redirect:/user/profile";
        }
        currentUser.setEmail(newEmail);
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("updateEmailMessage", "Email updated successfully.");
        return "redirect:/user/profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal User currentUser, String newPassword, String confirmPassword, RedirectAttributes redirectAttributes) {
        if (newPassword == null || newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("updatePasswordMessage", "Password must be at least 8 characters long.");
            return "redirect:/user/profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("updatePasswordMessage", "Password and confirmation do not match.");
            return "redirect:/user/profile";
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        currentUser.setPassword(encodedPassword);
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("updatePasswordMessage", "Password updated successfully.");
        return "redirect:/user/profile";
    }
}

