package com.streamcore.playback;

import com.streamcore.user.User;
import com.streamcore.content.Content;

public class PlaybackSession {
    private final User user;
    private final Content content;
    private int currentPosition;
    private boolean playing;

    public PlaybackSession(User user, Content content) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        if (!user.canAccess(content)) {
            throw new IllegalStateException("User does not have access to this content.");
        }
        this.user = user;
        this.content = content;
        this.currentPosition = 0;
        this.playing = false;
    }

    public void start() {
        this.playing = true;
        this.currentPosition = 0;
    }

    public void pause() {
        this.playing = false;
    }

    public void resume() {
        this.playing = true;
    }

    public void seek(int positionInSeconds) {
        int durationInSeconds = content.getDuration() * 60;
        if (positionInSeconds < 0 || positionInSeconds > durationInSeconds) {
            throw new IllegalArgumentException("Seek position is out of bounds.");
        }
        this.currentPosition = positionInSeconds;
    }

    public User getUser() {
        return user;
    }

    public Content getContent() {
        return content;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isPlaying() {
        return playing;
    }
}
