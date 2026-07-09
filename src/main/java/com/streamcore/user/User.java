package com.streamcore.user;

import com.streamcore.content.Content;

public abstract class User {
    private final String id;
    private final String username;
    private final String email;

    protected User(String id, String username, String email) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public abstract boolean canAccess(Content content);
}
