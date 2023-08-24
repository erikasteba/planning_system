package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Notes;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.NotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class NotesController {
    private static final Logger logger = LoggerFactory.getLogger(ActivitiesController.class);

    @Autowired
    private NotesRepository notesRepository;
    @PostMapping("/index/note")
    public String createNote(
            @RequestParam String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Notes note = new Notes(content,user);
        notesRepository.save(note);

        logger.info("New note created by user: {} and name {}", user.getId(), user.getName());
        return "redirect:/index";
    }

    @PostMapping("/index/note/delete")
    public String deleteNote(@RequestParam Long noteid) {
        Optional<Notes> note = notesRepository.findById(noteid);
        notesRepository.delete(note.get());
        logger.info("Note deleted, id: {}", noteid);
        return "redirect:/index";
    }

}
