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

    String errorMessage = "";
    String successMessage = "";

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
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("successMessage",successMessage);
        errorMessage="";
        successMessage="";
        return "friendship";
    }

    @PostMapping("/send-request")
    public String sendFriendRequest(@RequestParam Long senderId,
                                               @RequestParam String receiverEmail, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        model.addAttribute("userId", userId);
        try{
            User receiverUser = userRepository.findByEmail(receiverEmail);
            Long receiverId = receiverUser.getId();

            User friend = userRepository.findById(receiverId).orElse(null);

            if (receiverId.equals(userId)){
                errorMessage="You can not send requests to yourself.";
                model.addAttribute("errorMessage",errorMessage);
                return "redirect:/friendships/";
            }
            if(friendshipRepository.findStatusByUser1AndUser2(user, friend) == FriendshipStatus.PENDING){
                successMessage="Friend request was already sent!";
                model.addAttribute("successMessage",successMessage);
                return "redirect:/friendships/";
            }

            friendshipService.sendFriendRequest(senderId, receiverId);
            successMessage="Friend request sent!";
            logger.info("Friend request sent from: {} to: {}", senderId, receiverId);
            model.addAttribute("successMessage",successMessage);
            return "redirect:/friendships/";
        }catch (Exception e){
            errorMessage="The user with this email wasn't found.";
            model.addAttribute("errorMessage",errorMessage);
            return "redirect:/friendships/";
        }


    }

    @PostMapping("/accept-request")
    public String acceptFriendRequest(@RequestParam Long senderId,
                                      @RequestParam Long receiverId) {
        friendshipService.acceptFriendRequest(senderId, receiverId);
        logger.info("Friend request accepted by: {} with: {}", receiverId, senderId);
        return "redirect:/notifications";
    }

    @PostMapping("/decline-request")
    public String declineFriendRequest(@RequestParam Long senderId,
                                       @RequestParam Long receiverId) {
        friendshipService.declineFriendRequest(senderId, receiverId);
        logger.info("Friend request declined by: {} with: {}", receiverId, senderId);
        return "redirect:/notifications";
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
