package com.streamcore.platform;

import com.streamcore.content.Content;
import com.streamcore.user.User;
import com.streamcore.playback.PlaybackSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LoadBalancer {

    public static class PlatformNode {
        private final StreamPlatform platform;
        private final AtomicLong responseTimeNanos;

        public PlatformNode(StreamPlatform platform) {
            this.platform = platform;
            this.responseTimeNanos = new AtomicLong(0);
        }

        public StreamPlatform getPlatform() {
            return platform;
        }

        public long getResponseTimeNanos() {
            return responseTimeNanos.get();
        }

        public void updateResponseTime(long lastDurationNanos) {
            responseTimeNanos.updateAndGet(old -> (long) ((old * 0.9) + (lastDurationNanos * 0.1)));
        }
    }

    private final List<PlatformNode> nodes;
    private final Map<String, PlatformNode> userRouting;

    public LoadBalancer(int numInstances) {
        if (numInstances <= 0) {
            throw new IllegalArgumentException("Number of instances must be positive.");
        }
        List<PlatformNode> tempNodes = new ArrayList<>();
        for (int i = 0; i < numInstances; i++) {
            tempNodes.add(new PlatformNode(new StreamPlatform()));
        }
        this.nodes = Collections.unmodifiableList(tempNodes);
        this.userRouting = new ConcurrentHashMap<>();
    }

    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        long start = System.nanoTime();
        PlatformNode selected = selectLeastResponseTimeNode();
        selected.getPlatform().registerUser(user);
        userRouting.put(user.getId(), selected);
        long duration = System.nanoTime() - start;
        selected.updateResponseTime(duration);
    }

    public void addContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        for (PlatformNode node : nodes) {
            long start = System.nanoTime();
            node.getPlatform().addContent(content);
            long duration = System.nanoTime() - start;
            node.updateResponseTime(duration);
        }
    }

    public User getUser(String userId) {
        if (userId == null) {
            return null;
        }
        PlatformNode node = userRouting.get(userId);
        if (node == null) {
            return null;
        }
        long start = System.nanoTime();
        User user = node.getPlatform().getUser(userId);
        long duration = System.nanoTime() - start;
        node.updateResponseTime(duration);
        return user;
    }

    public Content getContent(String contentId, String userId) {
        if (contentId == null || userId == null) {
            return null;
        }
        PlatformNode node = userRouting.get(userId);
        if (node == null) {
            return null;
        }
        long start = System.nanoTime();
        Content content = node.getPlatform().getContent(contentId);
        long duration = System.nanoTime() - start;
        node.updateResponseTime(duration);
        return content;
    }

    public PlaybackSession createPlaybackSession(String userId, String contentId) {
        if (userId == null || contentId == null) {
            throw new IllegalArgumentException("User ID and Content ID cannot be null.");
        }
        PlatformNode node = userRouting.get(userId);
        if (node == null) {
            throw new IllegalStateException("User not registered on any platform node.");
        }
        long start = System.nanoTime();
        User user = node.getPlatform().getUser(userId);
        Content content = node.getPlatform().getContent(contentId);
        if (user == null || content == null) {
            throw new IllegalArgumentException("User or Content not found.");
        }
        PlaybackSession session = new PlaybackSession(user, content);
        long duration = System.nanoTime() - start;
        node.updateResponseTime(duration);
        return session;
    }

    public List<PlatformNode> getNodes() {
        return nodes;
    }

    private PlatformNode selectLeastResponseTimeNode() {
        PlatformNode leastNode = nodes.get(0);
        long leastTime = leastNode.getResponseTimeNanos();
        for (int i = 1; i < nodes.size(); i++) {
            PlatformNode node = nodes.get(i);
            long time = node.getResponseTimeNanos();
            if (time < leastTime) {
                leastTime = time;
                leastNode = node;
            }
        }
        return leastNode;
    }
}
