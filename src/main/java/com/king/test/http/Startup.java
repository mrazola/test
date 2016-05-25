package com.king.test.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.king.test.service.ScoreService;
import com.king.test.service.ScoreServiceMemoryImpl;
import com.king.test.service.session.SessionServiceCacheImpl;
import com.sun.net.httpserver.HttpServer;

/**
 * Main class to start {@code HttpServer}
 * 
 */
public class Startup {

    public static void main(String[] args) {
        
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            
            // this services could be instantiated by the dependency injection framework used, Guice or whatever
            ScoreService scoreService = new ScoreServiceMemoryImpl(new SessionServiceCacheImpl());
            RequestDispatcher dispatcher = new RequestDispatcher(scoreService);
            
            server.createContext("/", dispatcher);
            
            // A quick apache benchmark revealed that the default executor for requests (same thread starting the server)
            // was not able to meet high concurrency levels. Using  a separate thread pool improved that.
            server.setExecutor(Executors.newWorkStealingPool(4));
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start game scores server: " + e.getMessage());
        }
        
    }

}
