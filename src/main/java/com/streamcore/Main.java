package com.streamcore;

import com.streamcore.content.Movie;
import com.streamcore.content.Series;
import com.streamcore.content.Episode;
import com.streamcore.user.User;
import com.streamcore.user.RegularUser;
import com.streamcore.history.WatchEntry;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to StreamCore Video Platform   ");
        System.out.println("========================================");

        Movie inception = new Movie("M-101", "Inception", 148, "Christopher Nolan", false);
        Movie interstellar = new Movie("M-102", "Interstellar", 169, "Christopher Nolan", true);

        Series breakingBad = new Series("S-201", "Breaking Bad", false);
        Episode ep1 = new Episode("E-201-1", "Pilot", 1, 58, false);
        breakingBad.addEpisode(ep1);

        User regularUser = new RegularUser("U-001", "alice", "alice@example.com");

        System.out.println("\n--- Initial History State ---");
        printUserWatchHistory(regularUser);

        System.out.println("\n--- Adding Entries to Watch History ---");
        LocalDateTime now = LocalDateTime.of(2026, 7, 13, 22, 0);
        regularUser.getWatchHistory().addEntry(new WatchEntry(inception, now.minusDays(2)));
        regularUser.getWatchHistory().addEntry(new WatchEntry(ep1, now.minusHours(5)));

        printUserWatchHistory(regularUser);
    }

    private static void printUserWatchHistory(User user) {
        System.out.printf("Watch History for User: %s (%s)\n", user.getUsername(), user.getEmail());
        var entries = user.getWatchHistory().getEntries();
        if (entries.isEmpty()) {
            System.out.println("  No watch history recorded.");
            return;
        }
        for (WatchEntry entry : entries) {
            System.out.printf("  - Watched: %s | Time: %s | Duration: %d mins\n",
                    entry.getContent().getTitle(),
                    entry.getWatchedAt(),
                    entry.getContent().getDuration()
            );
        }
    }
}
