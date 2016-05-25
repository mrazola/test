package com.king.test.service.session;

import java.time.Clock;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * {@code LinkedHashMap} implementation that overrides <pre>removeEldestEntry</pre>
 *
 */
public class SessionCache extends LinkedHashMap<String, Session> {

	private static final long serialVersionUID = 7285924038185349796L;
	private static final int MAX_CAPACITY = 64000;
    private static final int INITIAL_CAPACITY = 32;
    private static final float LOAD_FACTOR = 0.75f;
    public static final int CLEANUP_KEYS = 16;

    private Clock clock;
    
    public SessionCache() {
        this(Clock.systemDefaultZone());
    }
    
    public SessionCache(Clock clock) {
        super(INITIAL_CAPACITY, LOAD_FACTOR, true);
        this.clock = clock;
    }
    
    /**
     * Cleans up to {@code SessionCache.CLEANUP_KEYS} after every insertion, and also keeps a maximum of {@code SessionCache.MAX_CAPACITY} elements.
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
}