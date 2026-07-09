package com.streamcore.subscription;

import com.streamcore.content.Content;

public interface SubscriptionPlan {
    String getPlanName();
    String getMaxVideoQuality();
    boolean canAccess(Content content);
}
