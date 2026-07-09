package com.streamcore;

import com.streamcore.content.Content;
import com.streamcore.content.Episode;
import com.streamcore.content.Movie;
import com.streamcore.content.Series;
import com.streamcore.user.User;
import com.streamcore.user.RegularUser;
import com.streamcore.user.PremiumUser;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to StreamCore Video Platform   ");
        System.out.println("========================================");

        Movie inception = new Movie("M-101", "Inception", 148, "Christopher Nolan", false);
        Movie interstellar = new Movie("M-102", "Interstellar", 169, "Christopher Nolan", true);

        Series breakingBad = new Series("S-201", "Breaking Bad", false);
        Episode ep1 = new Episode("E-201-1", "Pilot", 1, 58, false);
        Episode ep2 = new Episode("E-201-2", "Cat's in the Bag...", 2, 48, false);
        breakingBad.addEpisode(ep1);
        breakingBad.addEpisode(ep2);

        System.out.println("\n--- Catalog ---");
        printContentDetails(inception);
        printContentDetails(interstellar);
        printContentDetails(breakingBad);

        User regularUser = new RegularUser("U-001", "alice", "alice@example.com");
        User premiumUser = new PremiumUser("U-002", "bob", "bob@example.com");

        System.out.println("\n--- Access Control Verification ---");
        verifyAccess(regularUser, inception);
        verifyAccess(regularUser, interstellar);
        verifyAccess(regularUser, breakingBad);

        verifyAccess(premiumUser, inception);
        verifyAccess(premiumUser, interstellar);
        verifyAccess(premiumUser, breakingBad);
    }

    private static void verifyAccess(User user, Content content) {
        boolean access = user.canAccess(content);
        System.out.printf("User %s (%s) access to %s (Premium: %b): %s\n",
                user.getUsername(),
                user.getClass().getSimpleName(),
                content.getTitle(),
                content.isPremium(),
                access ? "GRANTED" : "DENIED"
        );
    }

    private static void printContentDetails(Content content) {
        System.out.printf("[%s] ID: %s, Title: %s, Duration: %d mins, Premium: %b\n",
                content.getClass().getSimpleName(),
                content.getId(),
                content.getTitle(),
                content.getDuration(),
                content.isPremium()
        );
    }
}
