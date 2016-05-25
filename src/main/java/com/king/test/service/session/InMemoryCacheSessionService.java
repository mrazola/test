package com.king.test.service.session;

import java.time.Clock;
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
 * @author mrazola
 * @created 23 May 2016
 */
public class InMemoryCacheSessionService implements SessionService {

	private static final int DEFAULT_TTL = 10 * 60 * 1000;
	
    private static final int INITIAL_CAPACITY = 32;
    private static final float LOAD_FACTOR = 0.75f;
    
    /**
     * 
     * TODO Comment
     *
     * @author mrazola
     * @created 23 May 2016
     */
    public class SessionCache extends LinkedHashMap<String, Session> {

        private static final int MAX_CAPACITY = 64000;
        public static final int CLEANUP_KEYS = 16;
        
        Clock clock;
        
        public SessionCache() {
            this(Clock.systemDefaultZone());
        }
        
        public SessionCache(Clock clock) {
            super(INITIAL_CAPACITY, LOAD_FACTOR, true);
            this.clock = clock;
        }
        
        /**
         * Cleans up to {@code Cache.CLEANUP_KEYS} after every insertion, and also keeps a maximum of {@code Cache.MAX_CAPACITY} elements.
         */
        @Override
        protected boolean removeEldestEntry(Entry<String, Session> eldest) {
            Iterator<Entry<String, Session>> it = this.entrySet().iterator();
            int i = 0;
            while (it.hasNext() && i < CLEANUP_KEYS) {
                Entry<String, Session> last = it.next();
                if (last.getValue().getExpiration() < clock.millis()) {
                    it.remove();
                } else if (this.size() > MAX_CAPACITY) {
                    return true;
                }
            }
            return false;
        }
    };
    
    Clock clock = Clock.systemDefaultZone();
    Map<String, Session> cache = Collections.synchronizedMap(new SessionCache());
    
    @Override
    public String login(Integer uid) {
        return this.login(uid, DEFAULT_TTL);
    }

    public String login(Integer uid, long ttl) {
        
        String sessionKey = UUID.randomUUID().toString();
        Session session = new Session(uid, System.currentTimeMillis() + ttl);
        this.cache.put(sessionKey, session);
        return sessionKey;
    }

    public boolean isSessionActive(String sessionKey) {
        Session session = this.cache.get(sessionKey);
        return session.getExpiration() >= clock.millis();
    }
    
    @Override
    public Optional<Session> getSession(String sessionKey) {
        Session session = this.cache.get(sessionKey);
        if (session == null) {
            return Optional.empty();
        } else if (session.getExpiration() < clock.millis()) {
            this.cache.remove(sessionKey); // if already expired, free cache
            return Optional.empty();
        } else {
            return Optional.of(session);
        }
    }

}
