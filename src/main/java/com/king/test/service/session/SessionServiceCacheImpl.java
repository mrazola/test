package com.king.test.service.session;

import java.time.Clock;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

/**
 * TODO Comment
 *
 */
public class SessionServiceCacheImpl implements SessionService {
	
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    
    private final Clock clock;
    private final Map<String, Session> cache;
    
    public SessionServiceCacheImpl() {
		this(Clock.systemUTC());
	}
    
    public SessionServiceCacheImpl(Clock clock) {
    	this.clock = clock;
    	this.cache = Collections.synchronizedMap(new SessionCache(clock));
    }
    
    @Override
    public String login(Integer uid) {
        return this.login(uid, DEFAULT_TTL);
    }

    /**
     * Performs an "unauthenticated" login for a supplied {@code Duration}
     * 
     * @param uid user id
     * @param ttl time to live for the session
     * @return a valid session key in the form of {@code UUID}
     */
    public String login(Integer uid, Duration ttl) {
        
        String sessionKey = UUID.randomUUID().toString();
        Session session = new Session(uid, Clock.offset(clock, ttl).millis());
        this.cache.put(sessionKey, session);
        return sessionKey;
    }

    public boolean isSessionActive(String sessionKey) {
        Session session = this.cache.get(sessionKey);
        return session.getExpiration() > clock.millis();
    }
    
    /**
     * 
     */
    @Override
    public Optional<Session> getSession(String sessionKey) {
        Session session = this.cache.get(sessionKey);
        if (session == null) {
            return Optional.empty();
        } else if (session.getExpiration() <= clock.millis()) {
            this.cache.remove(sessionKey); // if already expired, free cache
            return Optional.empty();
        } else {
            return Optional.of(session);
        }
    }

}
