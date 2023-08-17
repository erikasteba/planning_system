package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;

    @GetMapping("/user/profile")
    public String userProfile(
            //@AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        //String username = userDetails.getUsername();
        //User user = userRepository.findByUsername(username);
        //model.addAttribute("user", user);
        User dummyUser = new User("TestUser", "test@example.com", "Test User", "password");
        model.addAttribute("user", dummyUser);

        return "profile";
    }

    @PostMapping("/user/update-username")
    public String updateUsername(@AuthenticationPrincipal UserDetails userDetails, String newUsername) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        user.setUsername(newUsername);
        userRepository.save(user);
        return "redirect:/user/profile";
    }

    @PostMapping("/user/update-email")
    public String updateEmail(@AuthenticationPrincipal UserDetails userDetails, String newEmail) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        user.setEmail(newEmail);
        userRepository.save(user);
        return "redirect:/user/profile";
    }

    @PostMapping("/user/update-password")
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails, String newPassword) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        user.setPassword(newPassword);
        userRepository.save(user);
        return "redirect:/user/profile";
    }
}

