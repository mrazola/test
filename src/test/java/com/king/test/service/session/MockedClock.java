package com.king.test.service.session;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

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
