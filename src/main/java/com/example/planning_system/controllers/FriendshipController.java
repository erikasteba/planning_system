package com.example.planning_system.controllers;


import com.example.planning_system.enums.FriendshipStatus;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.FriendshipRepository;
import com.example.planning_system.repositories.UserRepository;
import com.example.planning_system.service.FriendshipService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/friendships")
@RequiredArgsConstructor
public class FriendshipController {
    private static final Logger logger = LoggerFactory.getLogger(ActivitiesController.class);

    private final FriendshipService friendshipService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        model.addAttribute("userId", userId);

        List<User> friends = friendshipService.listFriends(userId);
        model.addAttribute("friends", friends);


        Map<Long, FriendshipStatus> friendshipStatusMap = new HashMap<>();
        for (User friend : friends) {
            FriendshipStatus friendshipStatus = friendshipRepository.findStatusByUser1AndUser2(user, friend);
            friendshipStatusMap.put(friend.getId(), friendshipStatus);
        }
        model.addAttribute("friendshipStatusMap", friendshipStatusMap);

        return "friendship";
    }

    @PostMapping("/send-request")
    public String sendFriendRequest(@RequestParam Long senderId,
                                               @RequestParam Long receiverId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        model.addAttribute("userId", userId);

        friendshipService.sendFriendRequest(senderId, receiverId);
        logger.info("Friend request sent from: {} to: {}", senderId, receiverId);
        return "redirect:/friendships/";
    }

    @PostMapping("/accept-request")
    public String acceptFriendRequest(@RequestParam Long senderId,
                                      @RequestParam Long receiverId) {
        friendshipService.acceptFriendRequest(senderId, receiverId);
        logger.info("Friend request accepted by: {} with: {}", receiverId, senderId);
        return "friendship";
    }

    @PostMapping("/decline-request")
    public String declineFriendRequest(@RequestParam Long senderId,
                                       @RequestParam Long receiverId) {
        friendshipService.declineFriendRequest(senderId, receiverId);
        logger.info("Friend request declined by: {} with: {}", receiverId, senderId);
        return "friendship";
    }

    @GetMapping("/list")
    public String listFriends(@RequestParam Long userId) {
        List<User> friends = friendshipService.listFriends(userId);
        return "friendship";
    }

    @PostMapping("/unfriend/{friendId}")
    public String unfriend(@PathVariable Long friendId, @RequestParam("friendId") Long formFriendId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<User> friendUser = userRepository.findById(friendId);

        Long friendshipId = friendshipRepository.findIdByUser1AndUser2(user, friendUser.get());
        friendshipRepository.deleteById(friendshipId);
        logger.info("Friendship object deleted, id: {} by userId: {} (with userId: {})", friendshipId, user.getId(), friendId);

        return "redirect:/friendships/";

    }


}
