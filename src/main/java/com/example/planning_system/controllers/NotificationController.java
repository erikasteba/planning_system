package com.example.planning_system.controllers;

import com.example.planning_system.enums.FriendshipStatus;
import com.example.planning_system.models.Friendship;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.FriendshipRepository;
import com.example.planning_system.repositories.UserRepository;
import com.example.planning_system.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.planning_system.service.FriendshipService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    private final FriendshipService friendshipService;


    @GetMapping("/notifications")
    public String home(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        model.addAttribute("userId", userId);

        List<Friendship> friendRequests = friendshipRepository.findByUser2AndStatus(user, FriendshipStatus.PENDING);
        //System.out.println(friendRequests);
        model.addAttribute("friendRequests", friendRequests);

        List<Friendship> friendRequestsDeclined = friendshipRepository.findByUser1AndStatus(user, FriendshipStatus.DECLINED);
        //System.out.println(friendRequestsDeclined );
        model.addAttribute("friendRequestsDeclined", friendRequestsDeclined );


        return "notifications";
    }




}
