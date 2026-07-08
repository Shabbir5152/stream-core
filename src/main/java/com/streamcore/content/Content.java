package com.streamcore.content;

public abstract class Content {
    private final String id;
    private final String title;

    protected Content(String id, String title) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Content ID cannot be null or empty.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Content Title cannot be null or empty.");
        }
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public abstract int getDuration();
}
