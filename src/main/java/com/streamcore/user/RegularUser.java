package com.streamcore.user;

import com.streamcore.subscription.BasicPlan;

public class RegularUser extends User {

    public RegularUser(String id, String username, String email) {
        super(id, username, email, new BasicPlan());
    }
}
