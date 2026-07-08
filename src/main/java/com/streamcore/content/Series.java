package com.streamcore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Series extends Content {
    private final List<Episode> episodes;

    public Series(String id, String title) {
        super(id, title);
        this.episodes = new ArrayList<>();
    }

    public Series(String id, String title, List<Episode> episodes) {
        super(id, title);
        if (episodes == null) {
            throw new IllegalArgumentException("Episodes list cannot be null.");
        }
        this.episodes = new ArrayList<>(episodes);
    }

    public List<Episode> getEpisodes() {
        return Collections.unmodifiableList(episodes);
    }

    public void addEpisode(Episode episode) {
        if (episode == null) {
            throw new IllegalArgumentException("Episode cannot be null.");
        }
        this.episodes.add(episode);
    }

    @Override
    public int getDuration() {
        int totalDuration = 0;
        for (Episode episode : episodes) {
            totalDuration += episode.getDuration();
        }
        return totalDuration;
    }
}
