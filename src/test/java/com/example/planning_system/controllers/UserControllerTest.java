package com.example.planning_system.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.planning_system.models.User;
import com.example.planning_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setViewResolvers(viewResolver)
                .build();
    }


    @Test
    public void testConnectionToLogin() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));;
    }
    @Test
    public void testConnectionToRegistration() throws Exception {
        mockMvc.perform(get("/user/registration"))

                .andExpect(view().name("registration"));
    }
    @Test
    @WithMockUser(username = "nastja@gmail.com", password = "anastasija")
    public void testSuccessfulLogin() throws Exception {
        mockMvc.perform(post("/user/login")
                        .param("email", "nastja@gmail.com")
                        .param("password", "anastasija"))

                .andExpect(redirectedUrl("/index"));
    }

    @Test
    @WithMockUser(username = "nas@gmail.com", password = "12345678")
    public void testSuccessfulLRegistration() throws Exception {
        mockMvc.perform(post("/user/registration")
                        .param("email", "nas@gmail.com")
                        .param("password", "12345678")
                        .param("name", "nas"))
                .andExpect(redirectedUrl("/user/login"));

    }


}
