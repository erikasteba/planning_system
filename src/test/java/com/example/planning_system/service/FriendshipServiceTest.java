package com.example.planning_system.service;

import com.example.planning_system.enums.FriendshipStatus;
import com.example.planning_system.exceptions.FriendshipException;
import com.example.planning_system.models.Friendship;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.FriendshipRepository;
import com.example.planning_system.repositories.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private FriendshipRepository friendshipRepository;

    private FriendshipService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new FriendshipService(friendshipRepository, userRepository);
    }

    @Test
    public void testFindPendingFriendRequest_Success() {
        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        Friendship pendingFriendship = new Friendship();
        pendingFriendship.setUser1(sender);
        pendingFriendship.setUser2(receiver);
        pendingFriendship.setStatus(FriendshipStatus.PENDING);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(friendshipRepository.findByUser1AndUser2AndStatus(sender, receiver, FriendshipStatus.PENDING))
                .thenReturn(Optional.of(pendingFriendship));

        //Friendship result = underTest.findPendingFriendRequest(1L, 2L);

        //assertNotNull(result);
        //assertEquals(FriendshipStatus.PENDING, result.getStatus());
    }
    @Test
    public void testSendRequest() {

        User sender = new User("Main", "123@gmail.com", "123", "123");
        sender.setId(1L);
        User receiver = new User("extra", "321@gmail.com", "321", "123");
        receiver.setId(2L);

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(friendshipRepository.existsByUser1AndUser2(sender, receiver)).thenReturn(false);
        when(friendshipRepository.existsByUser1AndUser2(receiver, sender)).thenReturn(false);


        assertNotNull(userRepository);
        assertNotNull(underTest);

        underTest.sendFriendRequest(sender.getId(), receiver.getId());
        verify(friendshipRepository).save(any(Friendship.class));

    }

    @Test
    public void testListFriends_Success() {
        User user = new User();
        user.setId(1L);

        User friend1 = new User();
        friend1.setId(2L);

        User friend2 = new User();
        friend2.setId(3L);

        Friendship friendship1 = new Friendship();
        friendship1.setUser1(user);
        friendship1.setUser2(friend1);

        Friendship friendship2 = new Friendship();
        friendship2.setUser1(user);
        friendship2.setUser2(friend2);

        List<Friendship> friendships = new ArrayList<>();
        friendships.add(friendship1);
        friendships.add(friendship2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(friendshipRepository.findByUser1OrUser2(user, user)).thenReturn(friendships);

        List<User> result = underTest.listFriends(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(friend1));
        assertTrue(result.contains(friend2));
    }

    @Test
    public void testAcceptFriendRequest_Success() {
        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        Friendship pendingFriendship = new Friendship();
        pendingFriendship.setUser1(sender);
        pendingFriendship.setUser2(receiver);
        pendingFriendship.setStatus(FriendshipStatus.PENDING);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(friendshipRepository.findByUser1AndUser2AndStatus(sender, receiver, FriendshipStatus.PENDING))
                .thenReturn(Optional.of(pendingFriendship));

        underTest.acceptFriendRequest(1L, 2L);

        assertEquals(FriendshipStatus.ACCEPTED, pendingFriendship.getStatus());
        verify(friendshipRepository).save(pendingFriendship);
    }

    @Test
    public void testAcceptFriendRequest_PendingRequestNotFound() {
        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(friendshipRepository.findByUser1AndUser2AndStatus(sender, receiver, FriendshipStatus.PENDING))
                .thenReturn(Optional.empty());

        assertThrows(FriendshipException.class, () -> underTest.acceptFriendRequest(1L, 2L));
    }

    @Test
    public void testDeclineFriendRequest_Success() {
        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        Friendship pendingFriendship = new Friendship();
        pendingFriendship.setUser1(sender);
        pendingFriendship.setUser2(receiver);
        pendingFriendship.setStatus(FriendshipStatus.PENDING);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(friendshipRepository.findByUser1AndUser2AndStatus(sender, receiver, FriendshipStatus.PENDING))
                .thenReturn(Optional.of(pendingFriendship));

        underTest.declineFriendRequest(1L, 2L);

        assertEquals(FriendshipStatus.DECLINED, pendingFriendship.getStatus());
        verify(friendshipRepository).save(pendingFriendship);
    }

    @Test
    public void testDeclineFriendRequest_PendingRequestNotFound() {
        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(friendshipRepository.findByUser1AndUser2AndStatus(sender, receiver, FriendshipStatus.PENDING))
                .thenReturn(Optional.empty());

        assertThrows(FriendshipException.class, () -> underTest.declineFriendRequest(1L, 2L));
    }
}
