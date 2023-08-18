package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.service.FriendshipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping("/")
    public String home(Model model){
        return "friendship";
    }

    @PostMapping("/send-request")
    public String sendFriendRequest(@RequestParam Long senderId,
                                               @RequestParam Long receiverId) {
        // Assuming FriendshipRequestDto has fields like "senderId" and "receiverId"
        System.out.println("!");
        friendshipService.sendFriendRequest(senderId, receiverId);
        return "friendship";
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
}
