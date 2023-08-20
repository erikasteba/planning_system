package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.repositories.UserRepository;
import com.example.planning_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class ProfileController {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("/update-username")
    public String updateUsername(@AuthenticationPrincipal User currentUser, String newUsername) {
        currentUser.setName(newUsername);
        userService.updateUser(currentUser);
        return "redirect:/user/profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal User currentUser, String newEmail) {
        currentUser.setEmail(newEmail);
        userService.updateUser(currentUser);
        return "redirect:/user/profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal User currentUser, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        currentUser.setPassword(encodedPassword);
        userService.updateUser(currentUser);
        return "redirect:/user/profile";
    }
}

