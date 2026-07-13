package com.streamcore;

import com.streamcore.content.Content;
import com.streamcore.content.Movie;
import com.streamcore.content.Series;
import com.streamcore.user.User;
import com.streamcore.user.RegularUser;
import com.streamcore.user.PremiumUser;
import com.streamcore.platform.StreamPlatform;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to StreamCore Video Platform   ");
        System.out.println("========================================");

        StreamPlatform platform = new StreamPlatform();

        Movie inception = new Movie("M-101", "Inception", 148, "Christopher Nolan", false);
        Movie interstellar = new Movie("M-102", "Interstellar", 169, "Christopher Nolan", true);
        Series breakingBad = new Series("S-201", "Breaking Bad", false);

        platform.addContent(inception);
        platform.addContent(interstellar);
        platform.addContent(breakingBad);

        User alice = new RegularUser("U-001", "alice", "alice@example.com");
        User bob = new PremiumUser("U-002", "bob", "bob@example.com");

        platform.registerUser(alice);
        platform.registerUser(bob);

        System.out.println("\n--- Displaying Platform Catalog ---");
        for (Content item : platform.getCatalog()) {
            System.out.printf("  - [%s] ID: %s | Title: %s | Duration: %d mins | Premium: %b\n",
                    item.getClass().getSimpleName(),
                    item.getId(),
                    item.getTitle(),
                    item.getDuration(),
                    item.isPremium()
            );
        }

        System.out.println("\n--- Displaying Platform Users ---");
        for (User user : platform.getUsers()) {
            System.out.printf("  - User: %s | Email: %s | Plan: %s\n",
                    user.getUsername(),
                    user.getEmail(),
                    user.getSubscriptionPlan().getPlanName()
            );
        }

        System.out.println("\n--- Performing Catalog Lookup ---");
        Content searchedContent = platform.getContent("M-102");
        if (searchedContent != null) {
            System.out.printf("  Found: %s (Premium: %b)\n", searchedContent.getTitle(), searchedContent.isPremium());
        }

        User searchedUser = platform.getUser("U-001");
        if (searchedUser != null) {
            System.out.printf("  Found User: %s\n", searchedUser.getUsername());
        }
    }
}
