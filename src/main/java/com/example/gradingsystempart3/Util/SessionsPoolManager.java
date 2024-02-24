package com.example.gradingsystempart3.Util;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionsPoolManager {
    private static final int MAX_ACTIVE_SESSIONS = 100;
    private static final AtomicInteger activeSessions = new AtomicInteger(0);

    public static synchronized boolean canCreateSession() {
        return activeSessions.get() < MAX_ACTIVE_SESSIONS;
    }

    public static synchronized void incrementSessionCounter() {
        activeSessions.incrementAndGet();
    }

    public static synchronized void decrementSessionCounter() {
        activeSessions.decrementAndGet();
    }
}
