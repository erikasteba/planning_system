package com.example.planning_system.controllers;


import com.example.planning_system.enums.FriendshipStatus;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.FriendshipRepository;
import com.example.planning_system.repositories.UserRepository;
import com.example.planning_system.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;


    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

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

        System.out.println("!");
        friendshipService.sendFriendRequest(senderId, receiverId);
        return "redirect:/friendships/";
    }

    @PostMapping("/accept-request")
    public String acceptFriendRequest(@RequestParam Long senderId,
                                      @RequestParam Long receiverId) {
        friendshipService.acceptFriendRequest(senderId, receiverId);
        return "friendship";
    }

    @PostMapping("/decline-request")
    public String declineFriendRequest(@RequestParam Long senderId,
                                       @RequestParam Long receiverId) {
        friendshipService.declineFriendRequest(senderId, receiverId);
        return "friendship";
    }

    @GetMapping("/list")
    public String listFriends(@RequestParam Long userId) {
        List<User> friends = friendshipService.listFriends(userId);
        for (User friend : friends) {
            System.out.println(friend.getName());
        }
        return "friendship";
    }

    // Other methods for managing friendships, such as unfriending, blocking, etc.

    @PostMapping("/unfriend/{friendId}")
    public String unfriend(@PathVariable Long friendId, @RequestParam("friendId") Long formFriendId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<User> friendUser = userRepository.findById(friendId);

        Long friendshipId = friendshipRepository.findIdByUser1AndUser2(user, friendUser.get());
        friendshipRepository.deleteById(friendshipId);

        return "redirect:/friendships/";

    }


}
