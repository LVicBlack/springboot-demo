package com.websocket.sockjs.demo.config;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Vic on 2018/7/16
 */
public class SocketSessionRegistry {

    private final ConcurrentMap<String, Set<String>> userSessionIds = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    public SocketSessionRegistry() {
    }

    // 获取sessionIds
    public Set<String> getSessionIds(String user) {
        Set set = this.userSessionIds.get(user);
        return set != null ? set : Collections.emptySet();
    }

    public ConcurrentMap<String, Set<String>> getAllSessionIds() {
        return this.userSessionIds;
    }

    // register session
    public void registerSessionId(String user, String sessionId) {
        Assert.notNull(user, "User must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        Object var3 = this.lock;
        synchronized (this.lock) {
            Object set = (Set) this.userSessionIds.get(user);
            if (set == null) {
                set = new CopyOnWriteArraySet();
                this.userSessionIds.put(user, (Set<String>) set);
            }

            ((Set) set).add(sessionId);
        }
    }

    public void unregisterSessionId(String userName, String sessionId) {
        Assert.notNull(userName, "User Name must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        Object var3 = this.lock;
        synchronized (this.lock) {
            Set set = (Set) this.userSessionIds.get(userName);
            if (set != null && set.remove(sessionId) && set.isEmpty()) {
                this.userSessionIds.remove(userName);
            }

        }
    }


}
