package com.king.test.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.king.test.service.score.ScoreServiceMemoryImpl;
import com.king.test.service.session.InMemoryCacheSessionService;


/**
 * Unit tests for {@code RequestDispatcher}
 *
 */
public class TestRequestDispatcher {

    @Test
    public void dispatchingTest() throws URISyntaxException, IOException {
        
        MockedHttpExchange httpExchange = new MockedHttpExchange();
        httpExchange.reset(new URI("http://localhost:8080/fooo"), "");
        RequestDispatcher dispatcher = new RequestDispatcher(new InMemoryCacheSessionService(), new ScoreServiceMemoryImpl());

        dispatcher.handle(httpExchange);
        Assert.assertEquals(400, httpExchange.getResponseCode());
        
        httpExchange.reset(new URI("http://localhost:8080/1/login"), "");
        dispatcher.handle(httpExchange);
        Assert.assertEquals(200, httpExchange.getResponseCode());
        String sessionKey = httpExchange.getResponseBody().toString();
    }
    
}
