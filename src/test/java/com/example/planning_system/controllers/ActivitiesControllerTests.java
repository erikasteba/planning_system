package com.example.planning_system.controllers;

import com.example.planning_system.controllers.ActivitiesController;
import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@RunWith(SpringRunner.class)
@WebMvcTest(ActivitiesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ActivitiesControllerTests {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ActivitiesRepository activitiesRepository;

    @InjectMocks
    private ActivitiesController activitiesController;

    private Model model;
    @Mock
    private Authentication authentication;
    @Mock
    private User user;
    @MockBean
    private SecurityContext securityContext;


    @Test
    @WithMockUser(username = "testUser")
    public void showActivityForm_authenticatedUser() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setId(1L);

        Activities activity1 = new Activities("smth", "2023-08-17T10:00", "2023-08-15T10:30", false);
        Activities activity2 = new Activities("gym", "2023-10-10T11:00", "2023-10-10T12:00", true);
        activity1.setUser(user);
        activity2.setUser(user);

        List<Activities> activities = Arrays.asList(activity1, activity2);
        when(authentication.getPrincipal()).thenReturn(user);
        when(activitiesRepository.findByUserId(user.getId())).thenReturn(activities);

        mockMvc.perform(get("/calendar/activities"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("activities"))
                .andExpect(view().name("day-details"))
                .andExpect(model().attribute("activities", activities));
    }


    @Test
    @WithMockUser(username = "testUser")
    public void createActivitytest() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setId(1L);
        when(authentication.getPrincipal()).thenReturn(user);
        //when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/calendar/activities")
                        .param("activity_name", "camp")
                        .param("start_time", "2023-08-20T10:00")
                        .param("end_time", "2023-08-30T12:00")
                        .param("is_public", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/calendar/activities"));

        ArgumentCaptor<Activities> activityCaptor = ArgumentCaptor.forClass(Activities.class);
        verify(activitiesRepository, times(1)).save(activityCaptor.capture());

        Activities savedActivity = activityCaptor.getValue();
        assertEquals("camp", savedActivity.getName());
    }




    @Test
    public void deleteActivity_ifIdDoesntExist() throws Exception{
        Activities activity = new Activities();
        activity.setId(1L);
        when(activitiesRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        mockMvc.perform(post("/calendar/activities/delete/{act_id}", 2L))
                .andExpect(status().isFound());
    }


    @Test
    public void deleteActivity_ifIdExists() throws Exception{
        Activities activity = new Activities("gym", "2023-10-10T11:00", "2023-10-10T12:00", true);
        activity.setId(1L);
        when(activitiesRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        mockMvc.perform(post("/calendar/activities/delete/{act_id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/calendar/activities"));
    }


    @Test
    @WithMockUser(username = "testUser")
    public void showActivityEditForm() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setId(1L);
        when(authentication.getPrincipal()).thenReturn(user);

        Activities activity = new Activities("act1", "2023-10-10T11:00", "2023-10-10T12:00", true);
        activity.setId(1L);
        activity.setUser(user);
        when(activitiesRepository.findById(activity.getId())).thenReturn(Optional.of(activity));

        mockMvc.perform(get("/calendar/activities/edit/{act_id}", activity.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("activities"))
                .andExpect(model().attribute("name", "act1"))
                .andExpect(view().name("edit-activities"));
    }



    @Test
    @WithMockUser(username = "testUser")
    public void editActivityTest() throws Exception{
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setId(1L);
        when(authentication.getPrincipal()).thenReturn(user);

        Activities activity = new Activities("act1", "2023-10-10T11:00", "2023-10-10T12:00", true);
        activity.setId(1L);
        activity.setUser(user);
        when(activitiesRepository.findById(activity.getId())).thenReturn(Optional.of(activity));

        mockMvc.perform(post("/calendar/activities/edit/{act_id}", activity.getId())
                        .param("activity_name", "act1_new")
                        .param("start_time", "2023-08-20T10:00")
                        .param("end_time", "2023-08-30T12:00")
                        .param("isPublic", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/calendar/activities"));

        ArgumentCaptor<Activities> activityCaptor = ArgumentCaptor.forClass(Activities.class);
        verify(activitiesRepository, times(1)).save(activityCaptor.capture());

        Activities savedActivity = activityCaptor.getValue();
        assertEquals("act1_new", savedActivity.getName());
        assertFalse(savedActivity.isPublic());
    }





}

