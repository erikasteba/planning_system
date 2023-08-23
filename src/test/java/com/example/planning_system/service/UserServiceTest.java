package com.example.planning_system.service;

import com.example.planning_system.models.User;
import com.example.planning_system.repositories.ActivitiesRepository;
import com.example.planning_system.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        boolean result = underTest.createUser(user);

        assertTrue(result);
        assertTrue(user.isActive());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).saveAndFlush(user);
    }


    @Test
    public void testCreateUser_UserAlreadyExists() {
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(user);

        boolean result = underTest.createUser(user);

        assertFalse(result);
        verify(userRepository, never()).saveAndFlush(user);
    }
}