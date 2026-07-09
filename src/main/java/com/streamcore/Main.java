package com.streamcore;

import com.streamcore.content.Content;
import com.streamcore.content.Episode;
import com.streamcore.content.Movie;
import com.streamcore.content.Series;
import com.streamcore.user.User;
import com.streamcore.user.RegularUser;
import com.streamcore.user.PremiumUser;
import com.streamcore.playback.PlaybackSession;

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
        User premiumUser = new PremiumUser("U-002", "bob", "bob@example.com");

        System.out.println("\n--- Playback Verification: Authorized ---");
        PlaybackSession session = new PlaybackSession(regularUser, inception);
        printSessionState(session);

        System.out.println("\nStarting playback...");
        session.start();
        printSessionState(session);

        System.out.println("\nSeeking to 10 minutes (600 seconds)...");
        session.seek(600);
        printSessionState(session);

        System.out.println("\nPausing playback...");
        session.pause();
        printSessionState(session);

        System.out.println("\nResuming playback...");
        session.resume();
        printSessionState(session);

        System.out.println("\n--- Playback Verification: Unauthorized ---");
        try {
            System.out.println("Attempting to play premium content for RegularUser...");
            PlaybackSession failedSession = new PlaybackSession(regularUser, interstellar);
            failedSession.start();
        } catch (IllegalStateException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        System.out.println("\n--- Playback Verification: Seek Out of Bounds ---");
        try {
            System.out.println("Attempting to seek past movie duration...");
            session.seek(20000);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }
    }

    private static void printSessionState(PlaybackSession session) {
        System.out.printf("Session - User: %s | Content: %s | Position: %d sec | Playing: %b\n",
                session.getUser().getUsername(),
                session.getContent().getTitle(),
                session.getCurrentPosition(),
                session.isPlaying()
        );
    }
}
