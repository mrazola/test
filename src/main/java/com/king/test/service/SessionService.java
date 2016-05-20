package com.king.test.service;

/**
 * TODO Comment
 *
 */
public interface SessionService {
    
    /**
     * 
     * @return the session key
     */
    public String login(Integer uid);
    
    public boolean isSessionActive(String sessionKey);
    
}
