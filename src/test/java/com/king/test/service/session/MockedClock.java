package com.king.test.service.session;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

/**
 * To be used for testing, implements methods to manage time.
 * 
 */
public class MockedClock extends Clock {

	private Clock delegate;
	
	public MockedClock(Clock delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * Sets a new clock to delegate to
	 * @param delegate
	 */
	public void setClock(Clock delegate) {
		this.delegate = delegate;
	}

	/**
	 * Speeds up the delegated clock by the supplied offset
	 * 
	 * @param offset
	 */
	public void speedUpBy(Duration offset) {
		this.delegate = Clock.offset(delegate, offset);
	}
	
	@Override
	public ZoneId getZone() {
		return delegate.getZone();
	}

	@Override
	public Clock withZone(ZoneId zone) {
		return delegate.withZone(zone);
	}

	@Override
	public Instant instant() {
		return delegate.instant();
	}
	
	
}
