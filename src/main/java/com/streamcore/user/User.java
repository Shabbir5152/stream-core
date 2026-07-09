package com.streamcore.user;

import com.streamcore.subscription.SubscriptionPlan;
import com.streamcore.content.Content;

public abstract class User {
    private final String id;
    private final String username;
    private final String email;
    private SubscriptionPlan subscriptionPlan;

    protected User(String id, String username, String email, SubscriptionPlan subscriptionPlan) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (subscriptionPlan == null) {
            throw new IllegalArgumentException("Subscription plan cannot be null.");
        }
        this.id = id;
        this.username = username;
        this.email = email;
        this.subscriptionPlan = subscriptionPlan;
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

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        if (subscriptionPlan == null) {
            throw new IllegalArgumentException("Subscription plan cannot be null.");
        }
        this.subscriptionPlan = subscriptionPlan;
    }

    public boolean canAccess(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        return subscriptionPlan.canAccess(content);
    }
}
