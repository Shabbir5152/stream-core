package com.streamcore.user;

import com.streamcore.subscription.PremiumPlan;

public class PremiumUser extends User {

    public PremiumUser(String id, String username, String email) {
        super(id, username, email, new PremiumPlan());
    }
}
