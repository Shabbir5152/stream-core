package com.streamcore;

import com.streamcore.content.Content;
import com.streamcore.content.ContentFactory;
import com.streamcore.content.Episode;
import com.streamcore.content.Movie;
import com.streamcore.content.Series;
import com.streamcore.user.User;
import com.streamcore.user.UserFactory;
import com.streamcore.playback.PlaybackSession;
import com.streamcore.platform.StreamPlatform;
import com.streamcore.history.WatchEntry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to StreamCore Video Platform   ");
        System.out.println("========================================");

        ContentFactory contentFactory = new ContentFactory();
        UserFactory userFactory = new UserFactory();
        StreamPlatform platform = new StreamPlatform();

        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            User user = userFactory.createRegular("U-REG-" + i, "regular_user_" + i, "reg" + i + "@example.com");
            platform.registerUser(user);
            users.add(user);
        }
        for (int i = 1; i <= 50; i++) {
            User user = userFactory.createPremium("U-PREM-" + i, "premium_user_" + i, "prem" + i + "@example.com");
            platform.registerUser(user);
            users.add(user);
        }

        List<Content> catalogItems = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Movie movie = contentFactory.createMovie("M-REG-" + i, "Regular Movie " + i, 90 + i, "Director " + i, false);
            platform.addContent(movie);
            catalogItems.add(movie);
        }
        for (int i = 1; i <= 15; i++) {
            Movie movie = contentFactory.createMovie("M-PREM-" + i, "Premium Movie " + i, 100 + i, "Director " + i, true);
            platform.addContent(movie);
            catalogItems.add(movie);
        }

        for (int i = 1; i <= 10; i++) {
            Series series = contentFactory.createSeries("S-REG-" + i, "Regular Series " + i, false);
            for (int e = 1; e <= 5; e++) {
                Episode ep = contentFactory.createEpisode("E-REG-" + i + "-" + e, "Regular Ep " + e, e, 45, false);
                series.addEpisode(ep);
            }
            platform.addContent(series);
            catalogItems.add(series);
        }

        System.out.printf("\nInitialized Platform with %d Users and %d Catalog Items.\n",
                platform.getUsers().size(), platform.getCatalog().size());

        System.out.println("\n--- Starting High Dynamic Access Simulation ---");
        int totalRequests = 10000;
        int successfulSessions = 0;
        int deniedSessions = 0;
        long startTime = System.currentTimeMillis();

        Random random = new Random(42);
        LocalDateTime baseTime = LocalDateTime.of(2026, 7, 13, 23, 0);

        for (int i = 0; i < totalRequests; i++) {
            User user = users.get(random.nextInt(users.size()));
            Content content = catalogItems.get(random.nextInt(catalogItems.size()));

            try {
                PlaybackSession session = new PlaybackSession(user, content);
                session.start();
                if (random.nextBoolean()) {
                    session.seek(random.nextInt(content.getDuration() * 60));
                }
                session.pause();
                user.getWatchHistory().addEntry(new WatchEntry(content, baseTime.minusMinutes(random.nextInt(10080))));
                successfulSessions++;
            } catch (IllegalStateException e) {
                deniedSessions++;
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Simulation Completed.");
        System.out.printf("  Total Playback Requests: %d\n", totalRequests);
        System.out.printf("  Access Granted Sessions : %d\n", successfulSessions);
        System.out.printf("  Access Denied Sessions  : %d\n", deniedSessions);
        System.out.printf("  Execution Time          : %d ms\n", (endTime - startTime));

        System.out.println("\n--- Verifying Watch History Integrity ---");
        int totalHistoryRecords = 0;
        for (User user : platform.getUsers()) {
            totalHistoryRecords += user.getWatchHistory().getEntries().size();
        }
        System.out.printf("  Total Watch History Entries Across All Users: %d\n", totalHistoryRecords);
    }
}
