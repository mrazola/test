package com.king.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.king.test.http.RequestDispatcher;
import com.king.test.service.score.ScoreService;
import com.king.test.service.score.ScoreServiceMemoryImpl;
import com.king.test.service.session.InMemoryCacheSessionService;
import com.king.test.service.session.SessionService;
import com.sun.net.httpserver.HttpServer;

/**
 * TODO Comment
 * 
 */
public class Startup {

    public static void main(String[] args) {
        
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            SessionService sessionService = new InMemoryCacheSessionService();
            ScoreService scoreService = new ScoreServiceMemoryImpl();
            
            server.createContext("/", new RequestDispatcher(sessionService, scoreService));
            
            // A quick apache benchmark revealed that the default executor for requests (same thread starting the server) was not
            // able to meet high concurrency levels. Using  a separate thread pool improved that.
            server.setExecutor(Executors.newWorkStealingPool(4));
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start game scores server: " + e.getMessage());
        }
    }

}
