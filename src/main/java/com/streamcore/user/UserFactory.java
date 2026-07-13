package com.streamcore.user;

public class UserFactory {

    public User createRegular(String id, String username, String email) {
        return new RegularUser(id, username, email);
    }

    public User createPremium(String id, String username, String email) {
        return new PremiumUser(id, username, email);
    }
}
