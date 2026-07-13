package com.streamcore.playback;

import com.streamcore.content.Movie;
import com.streamcore.user.User;
import com.streamcore.user.RegularUser;
import com.streamcore.user.PremiumUser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaybackSessionTest {

    @Test
    public void testPlaybackSessionInitializationSuccess() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);

        PlaybackSession session = new PlaybackSession(user, movie);
        assertEquals(user, session.getUser());
        assertEquals(movie, session.getContent());
        assertEquals(0, session.getCurrentPosition());
        assertFalse(session.isPlaying());
    }

    @Test
    public void testPlaybackSessionInitializationValidationExceptions() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);

        assertThrows(IllegalArgumentException.class, () -> new PlaybackSession(null, movie));
        assertThrows(IllegalArgumentException.class, () -> new PlaybackSession(user, null));
    }

    @Test
    public void testPlaybackSessionAccessRestrictionsException() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie premiumMovie = new Movie("M-2", "Interstellar", 169, "Nolan", true);

        assertThrows(IllegalStateException.class, () -> new PlaybackSession(user, premiumMovie));
    }

    @Test
    public void testPlaybackSessionLifecycle() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);
        PlaybackSession session = new PlaybackSession(user, movie);

        session.start();
        assertTrue(session.isPlaying());
        assertEquals(0, session.getCurrentPosition());

        session.pause();
        assertFalse(session.isPlaying());

        session.resume();
        assertTrue(session.isPlaying());
    }

    @Test
    public void testPlaybackSeekOperations() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false); // 148 minutes = 8880 seconds
        PlaybackSession session = new PlaybackSession(user, movie);

        session.seek(500);
        assertEquals(500, session.getCurrentPosition());

        session.seek(0);
        assertEquals(0, session.getCurrentPosition());

        session.seek(8880);
        assertEquals(8880, session.getCurrentPosition());
    }

    @Test
    public void testPlaybackSeekValidationExceptions() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false); // 8880 seconds
        PlaybackSession session = new PlaybackSession(user, movie);

        assertThrows(IllegalArgumentException.class, () -> session.seek(-1));
        assertThrows(IllegalArgumentException.class, () -> session.seek(8881));
        assertThrows(IllegalArgumentException.class, () -> session.seek(10000));
    }
}
