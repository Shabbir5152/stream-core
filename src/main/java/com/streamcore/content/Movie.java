package com.streamcore.content;

public class Movie extends Content {
    private final int duration;
    private final String director;

    public Movie(String id, String title, int duration, String director, boolean premium) {
        super(id, title, premium);
        if (duration <= 0) {
            throw new IllegalArgumentException("Movie duration must be positive.");
        }
        if (director == null || director.isBlank()) {
            throw new IllegalArgumentException("Movie director cannot be null or empty.");
        }
        this.duration = duration;
        this.director = director;
    }

    public Movie(String id, String title, int duration, String director) {
        this(id, title, duration, director, false);
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public String getDirector() {
        return director;
    }
}
