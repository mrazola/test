package com.king.test.service.session;

import java.time.Clock;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * {@code LinkedHashMap} implementation that overrides <pre>removeEldestEntry</pre>, and
 * orders elements by access time, to be used as an LRU cache.
 *
 */
public class SessionCache extends LinkedHashMap<String, Session> {

	private static final long serialVersionUID = 7285924038185349796L;
	private static final int DEFAULT_MAX_CAPACITY = 64000;
	public static final int DEFAULT_CLEANUP_KEYS = 16;
    private static final int INITIAL_CAPACITY = 32;
    private static final float LOAD_FACTOR = 0.75f;

    private Clock clock;
    private int maxCapacity;
    private int cleanupKeys;
    
    public SessionCache() {
        this(Clock.systemDefaultZone());
    }
    
    public SessionCache(Clock clock) {
        this(clock, DEFAULT_MAX_CAPACITY);
    }
    
    public SessionCache(Clock clock, int maxCapacity) {
        this(clock, maxCapacity, DEFAULT_CLEANUP_KEYS);
    }
    
    public SessionCache(Clock clock, int maxCapacity, int cleanupKeys) {
        super(INITIAL_CAPACITY, LOAD_FACTOR, true); // order by access time
        this.clock = clock;
        this.maxCapacity = maxCapacity;
        this.cleanupKeys = cleanupKeys;
    }
    
    /**
     * Removes up to {@code SessionCache#cleanupKeys} after every insertion, if expired, 
     * and also keeps a maximum of {@code SessionCache#maxCapacity} elements.
     */
    @Override
    protected boolean removeEldestEntry(Entry<String, Session> eldest) {
        Iterator<Entry<String, Session>> it = this.entrySet().iterator();
        int i = 0;
        while (it.hasNext() && i < this.cleanupKeys) {
            Entry<String, Session> last = it.next();
            if (last.getValue().getExpiration() < clock.millis()) {
                it.remove();
            } else if (this.size() > this.maxCapacity) {
                return true;
            }
        }
        return false;
    }
}