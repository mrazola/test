package com.king.test.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.king.test.service.Record;
import com.king.test.service.ScoreService;
import com.king.test.service.SessionService;
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
    
    SessionService sessionService;
    ScoreService scoreService;
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream out = exchange.getResponseBody();
        String result = "";
        try {
            ClientRequest request = new ClientRequest(exchange);
            
            switch (request.getAction()) {
                case LOGIN:
                    result = sessionService.login(request.getId());
                    break;
                    
                case SCORE:
                    Integer level = request.getId();
                    Integer score = Integer.valueOf(request.getBody());
                    scoreService.insertScore(request.getId(), score);
                    break;
                    
                case HIGHSCORELIST:
                    List<Record> top = scoreService.getHighScoreList(request.getId());
//                    StringBuilder builder = new StringBuilder();
//                    if (!top.isEmpty()) {
//                        for (Record record : top) {
//                            builder.append(record.getUid()).append('=').append(record.getScore()).append(',');
//                        }
//                        builder.substring(0, builder.length() - 1);
//                    }
                    // transform to comma separated list
                    result = top.stream().map(r -> r.toString()).collect(Collectors.joining(","));
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
