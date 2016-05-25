package com.king.test.service.session;

import java.time.Clock;
import java.time.Duration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for {@code SessionService}
 *
 */
public class TestSessionService {

    @Test
    public void basicTest() {
    	MockedClock mockedClock = new MockedClock(Clock.systemUTC());
    	SessionServiceCacheImpl sessionService = new SessionServiceCacheImpl(mockedClock);
		
        String sessionkey1 = sessionService.login(1);
        String sessionkey2 = sessionService.login(2);
        Assert.assertTrue(sessionService.isSessionActive(sessionkey1));
        Assert.assertTrue(sessionService.isSessionActive(sessionkey2));
        mockedClock.speedUpBy(Duration.ofMinutes(1));
        // sessions still active after 2 minutes
        Assert.assertTrue(sessionService.isSessionActive(sessionkey1));
        Assert.assertTrue(sessionService.isSessionActive(sessionkey2));
        mockedClock.speedUpBy(Duration.ofMinutes(9));
        // sessions expired
        Assert.assertFalse(sessionService.isSessionActive(sessionkey1));
        Assert.assertFalse(sessionService.isSessionActive(sessionkey2));
    }
    
    @Test
    public void maxElementsTest() {
    	int maxSize = 3;
    	SessionCache cache = new SessionCache(Clock.systemUTC(), maxSize);
    	cache.put("foo", new Session(1, Clock.offset(Clock.systemUTC(), Duration.ofMinutes(10)).millis()));
    	Assert.assertEquals(1, cache.size());
    	cache.put("foo2", new Session(2, Clock.offset(Clock.systemUTC(), Duration.ofMinutes(10)).millis()));
    	cache.put("foo3", new Session(3, Clock.offset(Clock.systemUTC(), Duration.ofMinutes(10)).millis()));
    	cache.put("foo4", new Session(4, Clock.offset(Clock.systemUTC(), Duration.ofMinutes(10)).millis()));
    	Assert.assertEquals(maxSize, cache.size()); 
    	// the oldest element should have been removed, check...
    	Assert.assertTrue(!cache.keySet().contains("foo"));
    }
    
    @Test
    public void cleanupKeysTest() {
    	int maxSize = 10;
    	int cleanupKeys = 2;
    	MockedClock mockedClock = new MockedClock(Clock.systemUTC());
    	
    	SessionCache cache = new SessionCache(mockedClock, maxSize, cleanupKeys);
    	cache.put("foo", new Session(1, Clock.offset(mockedClock, Duration.ofMinutes(10)).millis()));
    	cache.put("foo2", new Session(2, Clock.offset(mockedClock, Duration.ofMinutes(10)).millis()));
    	// make them expire
    	mockedClock.speedUpBy(Duration.ofMinutes(10));
    	// but since there's no bg thread to release them, size should be still 2
    	Assert.assertEquals(2, cache.size());
    	// on insert, cleanup process should clean 2 of them
    	cache.put("foo4", new Session(4, Clock.offset(mockedClock, Duration.ofMinutes(10)).millis()));
    	// now size should be 1, and the only element should be foo4
    	Assert.assertEquals(1, cache.size());
    	Assert.assertTrue(cache.keySet().contains("foo4"));
    	
    }
}
