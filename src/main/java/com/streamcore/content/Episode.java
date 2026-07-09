package com.streamcore.content;

public class Episode extends Content {
    private final int episodeNumber;
    private final int duration;

    public Episode(String id, String title, int episodeNumber, int duration, boolean premium) {
        super(id, title, premium);
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Episode number must be positive.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Episode duration must be positive.");
        }
        this.episodeNumber = episodeNumber;
        this.duration = duration;
    }

    public Episode(String id, String title, int episodeNumber, int duration) {
        this(id, title, episodeNumber, duration, false);
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
