package com.king.test.service.session;

import java.util.Optional;

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
    
    public Optional<Session> getSession(String sessionKey);
    
}
