package com.example.planning_system.service;


import com.example.planning_system.models.User;
import com.example.planning_system.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailTest {


    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setEmail("test98@gmail.com");
        user.setUsername("test98");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        assertNotNull(userDetails);
        assertEquals("test98", userDetails.getUsername());

    }


}
