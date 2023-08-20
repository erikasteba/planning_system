package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.FriendshipRepository;
import com.example.planning_system.repositories.UserRepository;
import com.example.planning_system.service.ActivityService;
import com.example.planning_system.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.planning_system.service.WeekService.generateDateTimeList;
@Controller
@RequiredArgsConstructor
public class CalendarFriendsController {
    private final CalendarService calendarService;
    private final ActivityService activityService;

    @Autowired
    private ActivitiesRepository activitiesRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/calendar-week-friends")
    public String showCalendarWeekFr(
            @RequestParam(name = "week", required = false, defaultValue = "2023-W33") String week,
            Model model) {
        model.addAttribute("selectedWeek", week);
        String[] weekParts = week.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]);
        LocalDate startDate = LocalDate.ofYearDay(year, 1).with(java.time.temporal.TemporalAdjusters.firstInMonth(java.time.DayOfWeek.MONDAY));
        startDate = startDate.plusWeeks(weekNumber - 1);
        List<Integer> dateNumbers = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dateNumbers.add(startDate.getDayOfMonth());
            startDate = startDate.plusDays(1);}
        String month = startDate.getMonth().toString();
        int monthValue = startDate.getMonth().getValue();
        model.addAttribute("dateNumbers", dateNumbers);
        model.addAttribute("month", month);
        model.addAttribute("monthValue", monthValue);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        List<Activities> activities = activitiesRepository.findByUserId(userId);



        List<User> friends = friendshipRepository.findUser2IdsByUser1IdAndStatus(user); // list of friends of current user
        List<List<Activities>> AllFriendsActivity = new ArrayList<>(); // each friend will have his list of activities and this is the list of those lists
        System.out.println("List of friends ");
        for (User u: friends) { // loops through all of friends and adds their activites to a list
            System.out.println("! " + u.getName());
            List<Activities> friendsActivity = activitiesRepository.findByUserId(u.getId());;
            AllFriendsActivity.add(friendsActivity); // add friend's acitvities to a list
            for (Activities a: friendsActivity) { // just for printing and debugging
                System.out.println("! " + a.toString());
            }
        }
        System.out.println("");
        List<List<List<LocalDateTime>>> formattedFriendDateTimeLists = new ArrayList<>();

        for (List<Activities> list: AllFriendsActivity){
            List<List<LocalDateTime>> friendDateTimeLists = new ArrayList<>();
            for (Activities activity : list) {
                List<LocalDateTime> friendDateTimeList = generateDateTimeList(
                        activity.getStartDate(),
                        activity.getStartTime(),
                        activity.getEndDate(),
                        activity.getEndTime()
                );
                friendDateTimeLists.add(friendDateTimeList);
            }
            formattedFriendDateTimeLists.add(friendDateTimeLists);
        }
        model.addAttribute("formattedFriendDateTimeLists", formattedFriendDateTimeLists);
        model.addAttribute("AllFriendsActivities", AllFriendsActivity);
        List<List<LocalDateTime>> dateTimeLists = new ArrayList<>();

        for (Activities activity : activities) {
            List<LocalDateTime> dateTimeList = generateDateTimeList(
                    activity.getStartDate(),
                    activity.getStartTime(),
                    activity.getEndDate(),
                    activity.getEndTime()
            );
            dateTimeLists.add(dateTimeList);
        }
        model.addAttribute("dateTimeLists", dateTimeLists);
        model.addAttribute("activities", activities);

        for (int i = 0; i < activities.size(); i++) {
            Activities activity = activities.get(i);
            List<LocalDateTime> dateTimeList = dateTimeLists.get(i);
//            System.out.println("Name: " +  activity.getUser().getName() + " Activity: " + activity.getName());
            for (LocalDateTime dateTime : dateTimeList) {
//                System.out.println("Name: " +  activity.getUser().getName() + " " +dateTime);
            }
//            System.out.println();
        }


        return "calendar-week-friends";
    }

    @GetMapping("/btnNextWeekFriend")
    public RedirectView btnNextWeekFriend(
            @RequestParam(name = "week", defaultValue = "2023-W33") String week) {

        String[] weekParts = week.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]) + 1;

        String newWeek = String.format("%d-W%d", year, weekNumber);

        String newUrl = "http://localhost:8080/calendar-week-friends?week=" + newWeek;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(newUrl);

        return redirectView;
    }


    @GetMapping("/btnPreviousWeekFriend")
    public RedirectView btnPreviousWeekFriend(
            @RequestParam(name = "week", defaultValue = "2023-W33") String week) {

        String[] weekParts = week.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]) - 1;

        String newWeek = String.format("%d-W%d", year, weekNumber);

        String newUrl = "http://localhost:8080/calendar-week-friends?week=" + newWeek;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(newUrl);

        return redirectView;
    }

}
