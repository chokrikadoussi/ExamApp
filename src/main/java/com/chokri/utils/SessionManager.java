package com.chokri.utils;

import com.chokri.model.UserRole;

public class SessionManager {
    private static SessionManager instance;
    private UserRole currentRole;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentRole(UserRole role) {
        this.currentRole = role;
    }

    public UserRole getCurrentRole() {
        return currentRole;
    }

    public boolean isTeacher() {
        return currentRole == UserRole.TEACHER;
    }

    public boolean isStudent() {
        return currentRole == UserRole.STUDENT;
    }
}
