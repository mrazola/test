package com.king.test.service.session;

import java.time.Duration;
import java.util.Optional;

/**
 * TODO Comment
 *
 */
public interface SessionService {
    
	public static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    
	/**
     * 
     * @return the session key
     */
    public String login(Integer uid);
    
    /**
     * 
     * @param sessionKey
     * @return a valid {@code Session} if still active, or empty if not
     */
    public Optional<Integer> getSession(String sessionKey);
    
}
