package com.streamcore.content;

import java.util.List;

public class ContentFactory {

    public Movie createMovie(String id, String title, int duration, String director, boolean premium) {
        return new Movie(id, title, duration, director, premium);
    }

    public Series createSeries(String id, String title, boolean premium) {
        return new Series(id, title, premium);
    }

    public Series createSeries(String id, String title, List<Episode> episodes, boolean premium) {
        return new Series(id, title, episodes, premium);
    }

    public Episode createEpisode(String id, String title, int episodeNumber, int duration, boolean premium) {
        return new Episode(id, title, episodeNumber, duration, premium);
    }
}
