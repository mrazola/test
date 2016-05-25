package com.king.test.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.king.test.service.ScoreService;
import com.king.test.service.ranking.Record;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Front controller for HTTP requests. Transforms client requests into
 * concrete method calls to {@code ScoreService}
 *
 */
public class RequestDispatcher implements HttpHandler {

    public static final String LOGIN = "login";
    public static final String SCORE = "score";
    public static final String HIGHSCORELIST = "highscorelist";
    public static final String SESSIONKEY_PARAM = "sessionkey";
    
    final ScoreService scoreService;
    
    public RequestDispatcher(ScoreService scoreService) {
        super();
        this.scoreService = scoreService;
    }

    public Optional<String> handle(ClientRequest request) {
        
        switch (request.getAction()) {
            case LOGIN:
                String result = scoreService.login(request.getId());
                return Optional.of(result);
                
            case SCORE:
                Integer level = request.getId();
                Integer score = Integer.valueOf(request.getBody());
                String sessionkey = request.getParams().get(SESSIONKEY_PARAM);
                
                scoreService.insertScore(level, sessionkey, score);
                return Optional.empty();
                
            case HIGHSCORELIST:
                List<Record> top = scoreService.getHighScoreList(request.getId());
                // transform to comma separated list
                result = top.stream().map(r -> r.toString()).collect(Collectors.joining(","));
                return Optional.of(result);
                
            default:
                throw new IllegalArgumentException("No endpoint to handle request");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream out = exchange.getResponseBody();
        try {
            ClientRequest request = ClientRequest.builder().withHttpExchange(exchange).build();
            
            String result = this.handle(request).orElse("");
            
            exchange.sendResponseHeaders(200, result.isEmpty() ? -1 : result.length());
            out.write(result.getBytes());
            
        } catch (IllegalArgumentException | NoSuchElementException e) {
            String error = e.getMessage();
            exchange.sendResponseHeaders(400, error.length()); // 400 - client error
            out.write(error.getBytes());
        } catch (Exception e) {
            String error = e.getMessage();
            exchange.sendResponseHeaders(500, error.length()); // 500 - server error
            out.write(error.getBytes());
        } finally {
            exchange.close();
        }
    }
    
}
