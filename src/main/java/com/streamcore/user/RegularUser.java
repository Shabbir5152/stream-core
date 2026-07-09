package com.streamcore.user;

import com.streamcore.content.Content;

public class RegularUser extends User {

    public RegularUser(String id, String username, String email) {
        super(id, username, email);
    }

    @Override
    public boolean canAccess(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        return !content.isPremium();
    }
}
