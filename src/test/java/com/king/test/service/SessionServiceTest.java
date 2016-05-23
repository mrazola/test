package com.king.test.service;

import org.junit.Test;

/**
 * TODO Comment
 *
 * @author mrazola
 * @created 23 May 2016
 */
public class SessionServiceTest {
    
    @Test
    public void testSessionService() throws InterruptedException {
        InMemoryCacheSessionService sessionService = new InMemoryCacheSessionService();
        
        sessionService.login(1, 100);
        sessionService.login(2, 200);
        Thread.sleep(1000);
        
        Thread.sleep(2000);
    }
}
