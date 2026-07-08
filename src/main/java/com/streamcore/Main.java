package com.streamcore;

import com.streamcore.content.Content;
import com.streamcore.content.Episode;
import com.streamcore.content.Movie;
import com.streamcore.content.Series;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Welcome to StreamCore Video Platform   ");
        System.out.println("========================================");

        System.out.println("\n[Creating Content: Movie]");
        Movie inception = new Movie("M-101", "Inception", 148, "Christopher Nolan");
        printContentDetails(inception);
        System.out.println("\n[Creating Content: Series & Episodes]");
        Series breakingBad = new Series("S-201", "Breaking Bad");
        
        Episode ep1 = new Episode("E-201-1", "Pilot", 1, 58);
        Episode ep2 = new Episode("E-201-2", "Cat's in the Bag...", 2, 48);
        Episode ep3 = new Episode("E-201-3", "...And the Bag's in the River", 3, 48);
        
        breakingBad.addEpisode(ep1);
        breakingBad.addEpisode(ep2);
        breakingBad.addEpisode(ep3);
        
        printContentDetails(breakingBad);

        System.out.println("\nEpisodes list inside Series:");
        for (Episode ep : breakingBad.getEpisodes()) {
            System.out.printf(" - Ep %d: %s (%d mins)\n", ep.getEpisodeNumber(), ep.getTitle(), ep.getDuration());
        }
    }

    private static void printContentDetails(Content content) {
        System.out.println("Content Details:");
        System.out.println("  ID      : " + content.getId());
        System.out.println("  Title   : " + content.getTitle());
        System.out.println("  Duration: " + content.getDuration() + " minutes");
        
        if (content instanceof Movie) {
            Movie movie = (Movie) content;
            System.out.println("  Director: " + movie.getDirector());
            System.out.println("  Type    : Standalone Movie");
        } else if (content instanceof Series) {
            Series series = (Series) content;
            System.out.println("  Episodes: " + series.getEpisodes().size());
            System.out.println("  Type    : Television Series");
        }
    }
}
