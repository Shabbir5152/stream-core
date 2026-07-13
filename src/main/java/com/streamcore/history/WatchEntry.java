package com.streamcore.history;

import com.streamcore.content.Content;
import java.time.LocalDateTime;

public class WatchEntry {
    private final Content content;
    private final LocalDateTime watchedAt;

    public WatchEntry(Content content, LocalDateTime watchedAt) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        if (watchedAt == null) {
            throw new IllegalArgumentException("Timestamp cannot be null.");
        }
        this.content = content;
        this.watchedAt = watchedAt;
    }

    public Content getContent() {
        return content;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }
}
