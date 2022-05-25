package com.aqu.takecare.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private boolean Supervisor;

    public LoggedInUser(String userId, String displayName, boolean supervisor) {
        this.userId = userId;
        this.displayName = displayName;
        Supervisor = supervisor;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSupervisor() {
        return Supervisor;
    }
}