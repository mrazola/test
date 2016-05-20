package com.king.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.king.test.http.RequestDispatcher;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * TODO Comment
 * 
 */
public class Startup {

    public static void main(String[] args) {
        
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", new RequestDispatcher());
            
            // A quick apache benchmark revealed that the default executor for requests (same thread starting the server) was not
            // able to meet high concurrency levels. Using  a separate thread pool improved that.
            server.setExecutor(Executors.newWorkStealingPool(2));
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start game scores server: " + e.getMessage());
        }
    }

    static void process(HttpExchange exchange) throws IOException {
        String response = "OK";
        OutputStream out = exchange.getResponseBody();

        try {

            exchange.sendResponseHeaders(200, response.length());

            out.write(response.getBytes());
            out.flush();
        } catch (Exception e) {
            String error = "KO";
            e.printStackTrace();
            exchange.sendResponseHeaders(500, error.length());
            out.write(error.getBytes());
        } finally {
            exchange.close();
        }
    }

}
