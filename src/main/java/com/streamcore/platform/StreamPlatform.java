package com.streamcore.platform;

import com.streamcore.content.Content;
import com.streamcore.user.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StreamPlatform {
    private final Map<String, User> users;
    private final Map<String, Content> catalog;

    public StreamPlatform() {
        this.users = new ConcurrentHashMap<>();
        this.catalog = new ConcurrentHashMap<>();
    }

    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("User with this ID is already registered.");
        }
        users.put(user.getId(), user);
    }

    public void addContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        if (catalog.containsKey(content.getId())) {
            throw new IllegalArgumentException("Content with this ID is already in catalog.");
        }
        catalog.put(content.getId(), content);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public Content getContent(String id) {
        return catalog.get(id);
    }

    public Collection<Content> getCatalog() {
        return Collections.unmodifiableCollection(catalog.values());
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(users.values());
    }
}
