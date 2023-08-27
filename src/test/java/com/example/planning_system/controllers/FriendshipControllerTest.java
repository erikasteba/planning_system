package com.example.planning_system.controllers;

import com.example.planning_system.models.User;
import com.example.planning_system.service.FriendshipService;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendshipControllerTest {
    private FriendshipController underTest;

    @Mock
    private FriendshipService friendshipService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new FriendshipController(friendshipService);
    }

    /*
    @Test
    public void testSendFriendRequest_Success() {
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setId(1L);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String viewName = underTest.sendFriendRequest(2L, "test@gmail.com", model);

        verify(friendshipService).sendFriendRequest(2L, 3L);  // Mage sure the service method is called with correct parameters
        verify(model).addAttribute("userId", 1L); // Make sure the userId attribute is added to the model
        assertEquals("redirect:/friendships/", viewName); // Check if the correct view name is returned
    }
*/

    @Test
    public void testAcceptFriendRequest_Success() {
        String viewName = underTest.acceptFriendRequest(2L, 3L);

        verify(friendshipService).acceptFriendRequest(2L, 3L); // Check if the service method is called with correct parameters
        assertEquals("friendship", viewName); // the correct view name is returned
    }

    @Test
    public void testDeclineFriendRequest_Success() {
        String viewName = underTest.declineFriendRequest(2L, 3L);

        verify(friendshipService).declineFriendRequest(2L, 3L); // Ensure the service method is called with correct parameters
        assertEquals("friendship", viewName); // Ensure the correct view name is returned
    }

    @Test
    public void testListFriends_Success() {
        List<User> friends = new ArrayList<>();
        User friend1 = new User();
        friend1.setName("Friend1");
        User friend2 = new User();
        friend2.setName("Friend2");
        friends.add(friend1);
        friends.add(friend2);

        when(friendshipService.listFriends(1L)).thenReturn(friends);

        String viewName = underTest.listFriends(1L);

        assertEquals("friendship", viewName); // Ensure the correct view name is returned
    }


}