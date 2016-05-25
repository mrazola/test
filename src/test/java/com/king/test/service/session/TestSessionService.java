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
	
	SessionServiceCacheImpl sessionService;
	MockedClock mockedClock;
	
	@Before
	public void setUp() {
		mockedClock = new MockedClock(Clock.systemUTC());
		sessionService = new SessionServiceCacheImpl(mockedClock);
	}
	
    @Test
    public void basicTest() {

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
}
