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
        currentUser.setName(newUsername);
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("updateUsernameMessage", "Username updated successfully.");
        return "redirect:/user/profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal User currentUser, String newEmail, RedirectAttributes redirectAttributes) {
        currentUser.setEmail(newEmail);
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("updateEmailMessage", "Email updated successfully.");
        return "redirect:/user/profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal User currentUser, String newPassword, RedirectAttributes redirectAttributes) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        currentUser.setPassword(encodedPassword);
        userService.updateUser(currentUser);
        redirectAttributes.addFlashAttribute("updatePasswordMessage", "Password updated successfully.");
        return "redirect:/user/profile";
    }
}

