package com.streamcore.platform;

import com.streamcore.content.Movie;
import com.streamcore.user.User;
import com.streamcore.user.RegularUser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StreamPlatformTest {

    @Test
    public void testPlatformRegistrationSuccess() {
        StreamPlatform platform = new StreamPlatform();
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);

        platform.registerUser(user);
        platform.addContent(movie);

        assertEquals(1, platform.getUsers().size());
        assertEquals(1, platform.getCatalog().size());
        assertEquals(user, platform.getUser("U-1"));
        assertEquals(movie, platform.getContent("M-1"));
    }

    @Test
    public void testPlatformRegistrationValidationExceptions() {
        StreamPlatform platform = new StreamPlatform();
        User user1 = new RegularUser("U-1", "alice", "alice@example.com");
        User user2 = new RegularUser("U-1", "duplicate_alice", "duplicate@example.com");

        Movie movie1 = new Movie("M-1", "Inception", 148, "Nolan", false);
        Movie movie2 = new Movie("M-1", "Duplicate Movie", 150, "Nolan", false);

        assertThrows(IllegalArgumentException.class, () -> platform.registerUser(null));
        assertThrows(IllegalArgumentException.class, () -> platform.addContent(null));

        platform.registerUser(user1);
        assertThrows(IllegalArgumentException.class, () -> platform.registerUser(user2));

        platform.addContent(movie1);
        assertThrows(IllegalArgumentException.class, () -> platform.addContent(movie2));
    }

    @Test
    public void testPlatformEncapsulation() {
        StreamPlatform platform = new StreamPlatform();
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie movie = new Movie("M-1", "Inception", 148, "Nolan", false);

        platform.registerUser(user);
        platform.addContent(movie);

        assertThrows(UnsupportedOperationException.class, () -> platform.getUsers().add(user));
        assertThrows(UnsupportedOperationException.class, () -> platform.getCatalog().add(movie));
    }
}
