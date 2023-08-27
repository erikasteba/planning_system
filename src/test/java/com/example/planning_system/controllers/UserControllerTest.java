package com.example.planning_system.controllers;

import com.example.planning_system.controllers.ActivitiesController;
import com.example.planning_system.models.Activities;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.UserRepository;
import com.example.planning_system.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private User user;


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Spy
    @InjectMocks
    private UserController userController;

    private Model model;
    @Mock
    private Authentication authentication;
    @MockBean
    private SecurityContext securityContext;

    private MockMultipartFile file1 = new MockMultipartFile("file", "filename.jpg", "text/plain", "some xml".getBytes());
    private MockMultipartFile file2 = new MockMultipartFile("file", "filename.html", "text/plain", "some xml".getBytes());


    @Test
    public void testCreateUser() throws Exception {

        Mockito.when(userService.createUser(any(User.class), any(MultipartFile.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/registration")
                        .file(file1)
                        .param("name", "name")
                        .param("email", "n@gmail.com")
                        .param("password", "12345678")
                        .param("confirmPassword", "12345678")

                )
                .andExpect(redirectedUrl("/user/login"));
    }
    @Test
    public void testCreateUserInvalid() throws Exception {

        User user = new User();
        user.setEmail("name2525gmail.com");
        user.setName("");
        user.setPassword("user123");

        Mockito.when(userService.createUser(Mockito.any(User.class),  any(MultipartFile.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/registration")
                        .file(file2)
                        .param("name", user.getName())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", "56")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorEmailMessage", "Email should contain @"))
                .andExpect(model().attribute("errorNameMessage", "Name can not be empty"))
                .andExpect(model().attribute("errorMessage", "This password must be 8-30 symbols"))
                .andExpect(model().attribute("errorPasMessage", "Passwords do not match"))
                .andExpect(model().attribute("errorFileMessage", "Image should be .jpg or .png"));

    }

    @Test
    public void testCreateUserExistingEmail() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(User.class),  any(MultipartFile.class))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/registration")
                        .file(file1)
                        .param("name", "name")
                        .param("email", "n@gmail.com")
                        .param("password", "12345678")
                        .param("confirmPassword", "12345678")

                )
                .andExpect(model().attribute("errorConfirmMessage", "User with this email already exists"));
    }





}
