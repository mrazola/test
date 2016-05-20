/*
 * Copyright (c) 2016 Ubisoft Mobile.
 * This program is not published or distributed in source code form; accordingly,
 * this source code is and remains confidential to and a trade secret of
 * Ubisoft Mobile.
 *
 * All rights reserved.
 */
package com.king.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * TODO Comment
 *
 * @author mrazola
 * @created 19 May 2016
 */
public class Startup {

    static AtomicLong counter = new AtomicLong();

    public static BlockingQueue<HttpExchange> processingQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {

        InetSocketAddress listenTo = new InetSocketAddress(8080);
        try {
            final HttpServer server = HttpServer.create(listenTo, 0);
            server.createContext("/", new HttpHandler() {

                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    // System.out.println(counter.getAndIncrement());
                    // processingQueue.offer(exchange);
                    process(exchange);
                }
            });
            
            // A quick apache benchmark revealed that the default executor for requests (same thread starting the server) was not
            // able to meet high concurrency levels. Using  a separate thread pool improved that.
            server.setExecutor(Executors.newWorkStealingPool(4));
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start game scores server");
            e.printStackTrace();
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
