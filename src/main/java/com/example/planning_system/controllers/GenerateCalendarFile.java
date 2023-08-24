package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.NotesRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class GenerateCalendarFile {
    @Autowired
    ActivitiesRepository activitiesRepository;

    @Autowired
    NotesRepository notesRepository;





    @GetMapping("/generate-ical")
    public void generateCalendarForGoogle(
            HttpServletResponse response,
            @RequestParam(required = false, defaultValue = "0") int timeZoneOffset   //TIME-ZONE HOUR FROM HTML INPUT
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        List<Activities> activities = activitiesRepository.findByUserId(userId);

        response.setContentType("text/calendar");
        response.setHeader("Content-Disposition", "attachment; filename=\"events.ics\"");

        OutputStream out = response.getOutputStream();
        out.write("BEGIN:VCALENDAR\n".getBytes());
        out.write("VERSION:2.0\n".getBytes());

        for (Activities activity : activities) {
            out.write("BEGIN:VEVENT\n".getBytes());
            out.write(("UID:" + activity.getId() + "\n").getBytes());

            //Method "minusHours" helps to convert time to the selected zone
            //EXAMPLE:
            //from our calendar it takes :   "sport 10:30"
            //but if selected zone was "+3"
            //in .isc file it will convert value to "sport 7:30"
            //it is ok, because google calendar will add +3hours based on selected zone there
            ZonedDateTime startZonedDateTime = ZonedDateTime.of(activity.getStartDate(), activity.getStartTime(), ZoneOffset.UTC).minusHours(timeZoneOffset);
            ZonedDateTime endZonedDateTime = ZonedDateTime.of(activity.getEndDate(), activity.getEndTime(), ZoneOffset.UTC).minusHours(timeZoneOffset);

            out.write(("DTSTAMP:" + startZonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")) + "\n").getBytes());
            out.write(("DTSTART:" + startZonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")) + "\n").getBytes());
            out.write(("DTEND:" + endZonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")) + "\n").getBytes());
            out.write(("SUMMARY:" + activity.getName() + "\n").getBytes());
            out.write("END:VEVENT\n".getBytes());
        }

        out.write("END:VCALENDAR\n".getBytes());
        out.flush();
    }
}
