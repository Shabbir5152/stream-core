package com.streamcore.subscription;

import com.streamcore.content.Content;

public class BasicPlan implements SubscriptionPlan {
    @Override
    public String getPlanName() {
        return "Basic";
    }

    @Override
    public String getMaxVideoQuality() {
        return "720p";
    }

    @Override
    public boolean canAccess(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        return !content.isPremium();
    }
}
