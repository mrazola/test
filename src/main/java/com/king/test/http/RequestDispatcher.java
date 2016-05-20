package com.king.test.http;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * TODO Comment
 *
 */
public class RequestDispatcher implements HttpHandler {

    public static final String LOGIN = "login";
    public static final String SCORE = "score";
    public static final String HIGHSCORELIST = "highscorelist";
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream out = exchange.getResponseBody();
        String result = "";
        try {
            ClientRequest request = new ClientRequest(exchange);
            
            switch (request.getAction()) {
                case LOGIN:
                    System.out.println("login");
                    break;
                case SCORE:
                    System.out.println("score");
                    Integer score = Integer.valueOf(request.getBody());
                    
                    System.out.println(score);
                    
                    break;
                case HIGHSCORELIST:
                    System.out.println("highscorelist");
                    break;
                default:
                    String error = "No endpoint to handle request";
                    exchange.sendResponseHeaders(400, error.length());
                    out.write(error.getBytes());
            }
            exchange.sendResponseHeaders(200, result.isEmpty() ? -1 : result.length());
            out.write(result.getBytes());
            
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            String error = "Could not parse request";
            exchange.sendResponseHeaders(400, error.length());
            out.write(error.getBytes());
        } finally {
            exchange.close();
        }
    }

}
