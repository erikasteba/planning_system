package com.example.planning_system.exceptions;

public class FriendshipException extends RuntimeException{
    public FriendshipException(String message) {
        super(message);
    }
    public FriendshipException(String message, Throwable cause) {
        super(message, cause);
    }
}
