package com.socialmediaapi.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String postId) {
        super(String.format("Products with id '%s' not found", postId));
    }
}
