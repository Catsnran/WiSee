package com.wisee.service;

import com.wisee.model.User;

public class SessionService {
    private static SessionService instance;
    private User currentUser;

    private SessionService() {}

    public static SessionService getInstance() {
        if (instance == null) instance = new SessionService();
        return instance;
    }

    public void login(User u)      { currentUser = u; }
    public void logout()           { currentUser = null; }
    public User getCurrentUser()   { return currentUser; }
    public boolean isLoggedIn()    { return currentUser != null; }
}
