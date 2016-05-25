package com.king.test.service.session;

import org.junit.Test;

/**
 * Unit tests for {@code SessionService}
 *
 */
public class TestSessionService {
    
    @Test
    public void testSessionService() throws InterruptedException {
        InMemoryCacheSessionService sessionService = new InMemoryCacheSessionService();
        
        sessionService.login(1, 100);
        sessionService.login(2, 200);
        Thread.sleep(1000);
        
        Thread.sleep(2000);
    }
}
