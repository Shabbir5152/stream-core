package com.streamcore;

import com.streamcore.content.Content;
import com.streamcore.content.ContentFactory;
import com.streamcore.content.Episode;
import com.streamcore.content.Movie;
import com.streamcore.content.Series;
import com.streamcore.user.User;
import com.streamcore.user.UserFactory;
import com.streamcore.playback.PlaybackSession;
import com.streamcore.platform.LoadBalancer;
import com.streamcore.history.WatchEntry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println(" Welcome to StreamCore Video Platform   ");
        System.out.println("========================================");

        ContentFactory contentFactory = new ContentFactory();
        UserFactory userFactory = new UserFactory();
        LoadBalancer loadBalancer = new LoadBalancer(5);

        List<String> userIds = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            String id = "U-REG-" + i;
            User user = userFactory.createRegular(id, "regular_user_" + i, "reg" + i + "@example.com");
            loadBalancer.registerUser(user);
            userIds.add(id);
        }
        for (int i = 1; i <= 50; i++) {
            String id = "U-PREM-" + i;
            User user = userFactory.createPremium(id, "premium_user_" + i, "prem" + i + "@example.com");
            loadBalancer.registerUser(user);
            userIds.add(id);
        }

        List<String> contentIds = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String id = "M-REG-" + i;
            Movie movie = contentFactory.createMovie(id, "Regular Movie " + i, 90 + i, "Director " + i, false);
            loadBalancer.addContent(movie);
            contentIds.add(id);
        }
        for (int i = 1; i <= 15; i++) {
            String id = "M-PREM-" + i;
            Movie movie = contentFactory.createMovie(id, "Premium Movie " + i, 100 + i, "Director " + i, true);
            loadBalancer.addContent(movie);
            contentIds.add(id);
        }
        for (int i = 1; i <= 10; i++) {
            String id = "S-REG-" + i;
            Series series = contentFactory.createSeries(id, "Regular Series " + i, false);
            for (int e = 1; e <= 5; e++) {
                Episode ep = contentFactory.createEpisode("E-REG-" + i + "-" + e, "Regular Ep " + e, e, 45, false);
                series.addEpisode(ep);
            }
            loadBalancer.addContent(series);
            contentIds.add(id);
        }

        System.out.println("\n--- Starting Multithreaded Load Balancer Simulation (50k Requests) ---");
        int totalRequests = 50000;
        ExecutorService executor = Executors.newFixedThreadPool(16);
        AtomicInteger successfulSessions = new AtomicInteger(0);
        AtomicInteger deniedSessions = new AtomicInteger(0);
        AtomicInteger errors = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();

        Random rand = new Random(42);
        LocalDateTime baseTime = LocalDateTime.of(2026, 7, 14, 0, 0);

        for (int i = 0; i < totalRequests; i++) {
            final String userId = userIds.get(rand.nextInt(userIds.size()));
            final String contentId = contentIds.get(rand.nextInt(contentIds.size()));
            final int seed = i;

            executor.submit(() -> {
                try {
                    User user = loadBalancer.getUser(userId);
                    Content content = loadBalancer.getContent(contentId, userId);
                    if (user == null || content == null) {
                        errors.incrementAndGet();
                        return;
                    }
                    try {
                        PlaybackSession session = loadBalancer.createPlaybackSession(userId, contentId);
                        session.start();
                        session.seek((seed % content.getDuration()) * 60);
                        session.pause();
                        user.getWatchHistory().addEntry(new WatchEntry(content, baseTime.minusMinutes(seed % 1000)));
                        successfulSessions.incrementAndGet();
                    } catch (IllegalStateException e) {
                        deniedSessions.incrementAndGet();
                    }
                } catch (Exception e) {
                    errors.incrementAndGet();
                }
            });
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(30, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();

        System.out.println("Simulation Completed.");
        System.out.printf("  Total Requests Processed: %d\n", totalRequests);
        System.out.printf("  Access Granted Sessions : %d\n", successfulSessions.get());
        System.out.printf("  Access Denied Sessions  : %d\n", deniedSessions.get());
        System.out.printf("  Errors / Misses         : %d\n", errors.get());
        System.out.printf("  Total Execution Time    : %d ms\n", (endTime - startTime));
        System.out.printf("  Platform Throughput    : %.2f req/sec\n",
                (double) totalRequests / ((double) (endTime - startTime) / 1000.0));

        System.out.println("\n--- Load Balancer Cluster Diagnostics ---");
        List<LoadBalancer.PlatformNode> nodes = loadBalancer.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            LoadBalancer.PlatformNode node = nodes.get(i);
            System.out.printf("  Node #%d | Response Time Metric: %d ns | Users Registered: %d\n",
                    i + 1,
                    node.getResponseTimeNanos(),
                    node.getPlatform().getUsers().size()
            );
        }

        int totalHistoryRecords = 0;
        for (String uId : userIds) {
            User u = loadBalancer.getUser(uId);
            if (u != null) {
                totalHistoryRecords += u.getWatchHistory().getEntries().size();
            }
        }
        System.out.printf("\n  Total Watch History Entries Across Cluster: %d\n", totalHistoryRecords);
    }
}
