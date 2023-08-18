package com.example.planning_system.service;

import com.example.planning_system.enums.FriendshipStatus;
import com.example.planning_system.exceptions.FriendshipException;
import com.example.planning_system.models.Friendship;
import com.example.planning_system.models.User;
import com.example.planning_system.repositories.FriendshipRepository;
import com.example.planning_system.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    @Autowired
    private final FriendshipRepository friendshipRepository;
    @Autowired
    private final UserRepository userRepository;

    private Friendship findPendingFriendRequest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + senderId + " not found."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + receiverId + " not found."));;
        return friendshipRepository.findByUser1AndUser2AndStatus(sender, receiver, FriendshipStatus.PENDING)
                .orElseThrow(() -> new FriendshipException("Pending friend request not found."));
    }

    public void sendFriendRequest(Long senderId, Long receiverId) {
        // Check if sender and receiver exist
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + senderId + " not found."));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + receiverId + " not found."));

        // Check if a friend request already exists
        if (friendshipRepository.existsByUser1AndUser2(sender, receiver) ||
                friendshipRepository.existsByUser1AndUser2(receiver, sender)) {
            throw new FriendshipException("Friend request already exists.");
        }

        // Create and save the friend request
        Friendship friendship = new Friendship();
        friendship.setUser1(sender);
        friendship.setUser2(receiver);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setCreatedAt(LocalDateTime.now());

        friendshipRepository.save(friendship);
    }
    public List<User> listFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found."));

        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, user);
        List<User> friends = new ArrayList<>();

        for (Friendship friendship : friendships) {
            User friendUser = (friendship.getUser1().equals(user)) ? friendship.getUser2() : friendship.getUser1();
            friends.add(friendUser);
        }

        return friends;
    }

    public void acceptFriendRequest(Long senderId, Long receiverId) {
        Friendship friendship = findPendingFriendRequest(senderId, receiverId);

        friendship.setStatus(FriendshipStatus.ACCEPTED);

        friendshipRepository.save(friendship);
    }

    public void declineFriendRequest(Long senderId, Long receiverId) {
        Friendship friendship = findPendingFriendRequest(senderId, receiverId);

        friendship.setStatus(FriendshipStatus.DECLINED);

        friendshipRepository.save(friendship);
    }


}
