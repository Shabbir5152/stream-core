package com.streamcore.subscription;

import com.streamcore.content.Content;

public class PremiumPlan implements SubscriptionPlan {
    @Override
    public String getPlanName() {
        return "Premium";
    }

    @Override
    public String getMaxVideoQuality() {
        return "4K UHD";
    }

    @Override
    public boolean canAccess(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        return true;
    }
}
