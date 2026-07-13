package com.streamcore.content;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ContentTest {

    @Test
    public void testMovieCreationSuccess() {
        Movie movie = new Movie("M-1", "Inception", 148, "Christopher Nolan", true);
        assertEquals("M-1", movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(148, movie.getDuration());
        assertEquals("Christopher Nolan", movie.getDirector());
        assertTrue(movie.isPremium());
    }

    @Test
    public void testMovieCreationValidationExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new Movie(null, "Inception", 148, "Christopher Nolan", true));
        assertThrows(IllegalArgumentException.class, () -> new Movie("  ", "Inception", 148, "Christopher Nolan", true));
        assertThrows(IllegalArgumentException.class, () -> new Movie("M-1", null, 148, "Christopher Nolan", true));
        assertThrows(IllegalArgumentException.class, () -> new Movie("M-1", "  ", 148, "Christopher Nolan", true));
        assertThrows(IllegalArgumentException.class, () -> new Movie("M-1", "Inception", 0, "Christopher Nolan", true));
        assertThrows(IllegalArgumentException.class, () -> new Movie("M-1", "Inception", -10, "Christopher Nolan", true));
        assertThrows(IllegalArgumentException.class, () -> new Movie("M-1", "Inception", 148, null, true));
        assertThrows(IllegalArgumentException.class, () -> new Movie("M-1", "Inception", 148, "   ", true));
    }

    @Test
    public void testEpisodeCreationSuccess() {
        Episode episode = new Episode("E-1", "Pilot", 1, 58, false);
        assertEquals("E-1", episode.getId());
        assertEquals("Pilot", episode.getTitle());
        assertEquals(1, episode.getEpisodeNumber());
        assertEquals(58, episode.getDuration());
        assertFalse(episode.isPremium());
    }

    @Test
    public void testEpisodeCreationValidationExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new Episode(null, "Pilot", 1, 58, false));
        assertThrows(IllegalArgumentException.class, () -> new Episode("E-1", "  ", 1, 58, false));
        assertThrows(IllegalArgumentException.class, () -> new Episode("E-1", "Pilot", 0, 58, false));
        assertThrows(IllegalArgumentException.class, () -> new Episode("E-1", "Pilot", -1, 58, false));
        assertThrows(IllegalArgumentException.class, () -> new Episode("E-1", "Pilot", 1, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new Episode("E-1", "Pilot", 1, -10, false));
    }

    @Test
    public void testSeriesCompositionAndDuration() {
        Series series = new Series("S-1", "Breaking Bad", false);
        assertEquals(0, series.getDuration());
        assertTrue(series.getEpisodes().isEmpty());

        Episode ep1 = new Episode("E-1", "Pilot", 1, 58, false);
        Episode ep2 = new Episode("E-2", "Cat's in the Bag", 2, 48, false);

        series.addEpisode(ep1);
        series.addEpisode(ep2);

        assertEquals(2, series.getEpisodes().size());
        assertEquals(106, series.getDuration());
    }

    @Test
    public void testSeriesConstructorWithList() {
        Episode ep1 = new Episode("E-1", "Pilot", 1, 58, false);
        Episode ep2 = new Episode("E-2", "Cat's in the Bag", 2, 48, false);
        List<Episode> episodes = Arrays.asList(ep1, ep2);

        Series series = new Series("S-1", "Breaking Bad", episodes, false);
        assertEquals(106, series.getDuration());
        assertEquals(2, series.getEpisodes().size());
    }

    @Test
    public void testSeriesValidationExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new Series("S-1", "Breaking Bad", null, false));
        Series series = new Series("S-1", "Breaking Bad", false);
        assertThrows(IllegalArgumentException.class, () -> series.addEpisode(null));
    }

    @Test
    public void testSeriesEncapsulation() {
        Series series = new Series("S-1", "Breaking Bad", false);
        Episode ep1 = new Episode("E-1", "Pilot", 1, 58, false);
        series.addEpisode(ep1);

        assertThrows(UnsupportedOperationException.class, () -> series.getEpisodes().add(new Episode("E-2", "Test", 2, 30, false)));
    }

    @Test
    public void testContentFactoryInstantiation() {
        ContentFactory factory = new ContentFactory();
        Movie movie = factory.createMovie("M-1", "Inception", 148, "Christopher Nolan", true);
        assertNotNull(movie);
        assertEquals("Inception", movie.getTitle());

        Series series = factory.createSeries("S-1", "Breaking Bad", false);
        assertNotNull(series);
        assertEquals("Breaking Bad", series.getTitle());

        Episode ep = factory.createEpisode("E-1", "Pilot", 1, 58, false);
        assertNotNull(ep);
        assertEquals("Pilot", ep.getTitle());

        List<Episode> list = new ArrayList<>();
        list.add(ep);
        Series seriesWithList = factory.createSeries("S-2", "Game of Thrones", list, true);
        assertNotNull(seriesWithList);
        assertEquals(58, seriesWithList.getDuration());
    }
}
