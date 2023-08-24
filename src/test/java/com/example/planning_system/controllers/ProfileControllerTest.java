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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ProfileController profileController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setViewResolvers(viewResolver)
                .build();
    }

// Username tests
    @Test
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void testUpdateUsername() {
        User currentUser = new User("testuser", "test@example.com", "Test User", "password");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        when(userService.existsByUsername("newUsername")).thenReturn(false);
        profileController.updateUsername(currentUser, "newUsername", redirectAttributes);
        verify(userService).updateUser(currentUser);
    }
    @Test
    @WithMockUser(username = "testUser", password = "testPassword")
    public void testUpdateUsernameEmpty() throws Exception {
        mockMvc.perform(post("/user/update-username")
                        .param("newUsername", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updateUsernameMessage"))
                .andExpect(flash().attribute("updateUsernameMessage", "Username cannot be empty."))
                .andExpect(redirectedUrl("/user/edit-profile"));
    }
    @Test
    public void testUpdateUsername_Success() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/update-username")
                .with(authentication(mock(Authentication.class)))
                .param("newUsername", "newUsername");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("updateUsernameMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("updateUsernameMessage", "Username updated successfully."))
                .andReturn();
        verify(userService, times(1)).updateUser(any(User.class));
    }
    @Test
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void testUpdateUsername_UsernameTaken() {
        User currentUser = new User("testuser", "test@example.com", "Test User", "password");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        when(userService.existsByUsername("takenUsername")).thenReturn(true);
        profileController.updateUsername(currentUser, "takenUsername", redirectAttributes);
        verify(userService, never()).updateUser(currentUser);
        assertEquals("Username is already taken.", redirectAttributes.getFlashAttributes().get("updateUsernameMessage"));
    }

// Name tests
    @Test
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void testUpdateName() throws Exception {
        User currentUser = new User("testuser", "test@example.com", "Test User", "password");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        when(userService.existsByEmail(anyString())).thenReturn(false);
        profileController.updateName(currentUser, "New Name", redirectAttributes);
        verify(userService).updateUser(eq(currentUser));
        mockMvc.perform(post("/user/update-name")
                        .param("newName", "New Name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updateNameMessage"))
                .andExpect(redirectedUrl("/user/edit-profile"));
    }
    @Test
    @WithMockUser(username = "testUser", password = "testPassword")
    public void testUpdateNameEmpty() throws Exception {
        mockMvc.perform(post("/user/update-name")
                        .param("newName", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updateNameMessage"))
                .andExpect(flash().attribute("updateNameMessage", "Name cannot be empty."))
                .andExpect(redirectedUrl("/user/edit-profile"));
    }
    @Test
    public void testUpdateName_Success() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/update-name")
                .with(authentication(mock(Authentication.class)))
                .param("newName", "newName");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("updateNameMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("updateNameMessage", "Name updated successfully."))
                .andReturn();
        verify(userService, times(1)).updateUser(any(User.class));
    }

// Email tests
    @Test
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void testUpdateEmail() {
        User currentUser = new User("testuser", "test@example.com", "Test User", "password");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        when(userService.existsByUsername("newEmail")).thenReturn(false);

        profileController.updateUsername(currentUser, "newUsername", redirectAttributes);

        verify(userService).updateUser(currentUser);

    }
    @Test
    @WithMockUser(username = "testUser", password = "testPassword")
    public void testUpdateEmailEmpty() throws Exception {
        mockMvc.perform(post("/user/update-email")
                        .param("newEmail", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updateEmailMessage"))
                .andExpect(flash().attribute("updateEmailMessage", "Email cannot be empty."))
                .andExpect(redirectedUrl("/user/edit-profile"));
    }
    @Test
    public void testUpdateEmail_Success() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/update-email")
                .with(authentication(mock(Authentication.class)))
                .param("newEmail", "newEmail@example.com");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("updateEmailMessage"))
                .andExpect(MockMvcResultMatchers.flash().attribute("updateEmailMessage", "Email updated successfully."))
                .andReturn();

        verify(userService, times(1)).updateUser(any(User.class));
    }
    @Test
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void testUpdateEmail_EmailTaken() {
        User currentUser = new User("testuser", "test@example.com", "Test User", "password");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        when(userService.existsByEmail("takenEmail@example.com")).thenReturn(true);
        profileController.updateEmail(currentUser, "takenEmail@example.com", redirectAttributes);
        verify(userService, never()).updateUser(currentUser);
        assertEquals("Email is already in use.", redirectAttributes.getFlashAttributes().get("updateEmailMessage"));
    }
    @Test
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void testUpdateEmail_EmailInvalidFormat() throws Exception {
        mockMvc.perform(post("/user/update-email")
                        .param("newEmail", "invalid-email-format"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updateEmailMessage"))
                .andExpect(flash().attribute("updateEmailMessage", "Invalid email format."))
                .andExpect(redirectedUrl("/user/edit-profile"));
    }

// Password tests
    @Test
    @WithMockUser(username = "testUser", password = "testPassword")
    public void testUpdatePassword_InvalidLength() throws Exception {
        mockMvc.perform(post("/user/update-password")
                        .param("newPassword", "short")
                        .param("confirmPassword", "short"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updatePasswordMessage"))
                .andExpect(flash().attribute("updatePasswordMessage", "Password must be at least 8 characters long."))
                .andExpect(redirectedUrl("/user/edit-profile"));
    }
    @Test
    @WithMockUser(username = "testUser", password = "testPassword")
    public void testUpdatePassword_Mismatch() throws Exception {
        mockMvc.perform(post("/user/update-password")
                        .param("newPassword", "newPassword123")
                        .param("confirmPassword", "differentPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updatePasswordMessage"))
                .andExpect(flash().attribute("updatePasswordMessage", "Password and confirmation do not match."))
                .andExpect(redirectedUrl("/user/edit-profile"));
    }
    @Test
    @WithMockUser(username = "testUser", password = "testPassword")
    public void testUpdatePassword_Success() throws Exception {
        MockHttpServletRequestBuilder request = post("/user/update-password")
                .with(authentication(mock(Authentication.class)))
                .param("newPassword", "newPassword123")
                .param("confirmPassword", "newPassword123");
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("updatePasswordMessage"))
                .andExpect(flash().attribute("updatePasswordMessage", "Password updated successfully."))
                .andReturn();
        verify(userService, times(1)).updateUser(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).updateUser(userCaptor.capture());

        User updatedUser = userCaptor.getValue();
        assertNotEquals("newPassword123", updatedUser.getPassword());
    }
}