package com.example.planning_system.controllers;

import com.example.planning_system.models.Activities;
import com.example.planning_system.models.Notes;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.NotesRepository;
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

    @Autowired
    private NotesRepository notesRepository;
    @PostMapping("/index/note")
    public String createNote(
            @RequestParam String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Notes note = new Notes(content,user);
        notesRepository.save(note);
        return "redirect:/index";
    }

    @PostMapping("/index/note/delete")
    public String deleteNote(@RequestParam Long noteid) {
        Optional<Notes> note = notesRepository.findById(noteid);
        notesRepository.delete(note.get());
        return "redirect:/index";
    }





}
