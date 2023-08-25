package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.FriendshipRepository;
import com.example.planning_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MapController {

    @Autowired
    private ActivitiesRepository activitiesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

    @GetMapping("/map")
    public String mainPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        List<Activities> activities = activitiesRepository.findByUserId(userId);
        List<User> friends = friendshipRepository.findFriends(user); // list of friends of current user
        List<List<Activities>> AllFriendsActivities = new ArrayList<>();

        for (User u: friends) { // loops through all of friends and adds their activites to a list
            System.out.println("! " + u.getName());
            List<Activities> friendsActivity = activitiesRepository.findByUserId(u.getId());;
            AllFriendsActivities.add(friendsActivity); // add friend's acitvities to a list
            for (Activities a: friendsActivity) { // just for printing and debugging
                System.out.println("! " + a.toString());
            }
        }
        model.addAttribute("friends", friends);
        model.addAttribute("activities", activities);
        model.addAttribute("AllFriendsActivities", AllFriendsActivities);
        return "map";
    }
}
