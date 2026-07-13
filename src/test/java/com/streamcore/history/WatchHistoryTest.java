package com.streamcore.history;

import com.streamcore.content.Movie;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class WatchHistoryTest {

    @Test
    public void testWatchEntryCreationSuccess() {
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);
        LocalDateTime now = LocalDateTime.now();

        WatchEntry entry = new WatchEntry(movie, now);
        assertEquals(movie, entry.getContent());
        assertEquals(now, entry.getWatchedAt());
    }

    @Test
    public void testWatchEntryValidationExceptions() {
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);
        LocalDateTime now = LocalDateTime.now();

        assertThrows(IllegalArgumentException.class, () -> new WatchEntry(null, now));
        assertThrows(IllegalArgumentException.class, () -> new WatchEntry(movie, null));
    }

    @Test
    public void testWatchHistoryOperations() {
        WatchHistory history = new WatchHistory();
        assertTrue(history.getEntries().isEmpty());

        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);
        WatchEntry entry1 = new WatchEntry(movie, LocalDateTime.now().minusDays(1));
        WatchEntry entry2 = new WatchEntry(movie, LocalDateTime.now());

        history.addEntry(entry1);
        history.addEntry(entry2);

        assertEquals(2, history.getEntries().size());
        assertEquals(entry1, history.getEntries().get(0));
        assertEquals(entry2, history.getEntries().get(1));
    }

    @Test
    public void testWatchHistoryValidationExceptions() {
        WatchHistory history = new WatchHistory();
        assertThrows(IllegalArgumentException.class, () -> history.addEntry(null));
    }

    @Test
    public void testWatchHistoryEncapsulation() {
        WatchHistory history = new WatchHistory();
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);
        WatchEntry entry = new WatchEntry(movie, LocalDateTime.now());
        history.addEntry(entry);

        assertThrows(UnsupportedOperationException.class, () -> history.getEntries().add(new WatchEntry(movie, LocalDateTime.now())));
    }
}
