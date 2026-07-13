package com.streamcore.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WatchHistory {
    private final List<WatchEntry> entries;

    public WatchHistory() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(WatchEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Watch entry cannot be null.");
        }
        this.entries.add(entry);
    }

    public List<WatchEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
