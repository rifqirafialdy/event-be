package com.example.event_be.auth.application.services;

public interface RbacService {
    boolean hasAccess(String userId, String screenCode, String actionCode);
}
