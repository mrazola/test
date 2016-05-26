package com.king.test.service.session;

import java.time.Duration;
import java.util.Optional;

/**
 * Provides a way of registering a user session, and check for validity
 *
 */
public interface SessionService {
    
	public static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    
	/**
     * Logs in a user a returns a session key, valid for {@code SessionService#DEFAULT_TTL}
     * 
     * @return the session key
     */
    public String login(Integer uid);
    
    /**
     * Gets a session for a user
     * 
     * @param sessionKey as provided in login
     * @return a user id if the session is still active, or empty if not
     */
    public Optional<Integer> getSession(String sessionKey);
    
}
