package com.king.test.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

/**
 * TODO Comment
 *
 */
public class ClientRequest {
    
    private final Integer id; // first path param
    private final String action; // second path param
    private final Map<String, String> params;
    private final String body;
    
    public ClientRequest(HttpExchange exchange) {
        try {
            URI uri = exchange.getRequestURI();
            String[] pathParams = uri.getPath().split("/");
            id = Integer.valueOf(pathParams[1]);
            action = pathParams[2];
            
            params = new HashMap<>();
            String query = uri.getQuery();
            if (query != null) {
                
                String[] reqParams = query.split("&");
                for (String param : reqParams) {
                    String[] parts = param.split("=");
                    params.put(parts[0], parts.length > 1 ? parts[1] : null); // http param has optional value
                }
            }
            
            InputStream in = exchange.getRequestBody();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            body = result.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not parse client request");
        }
    }
    
    public Integer getId() {
        return id;
    }
    public String getAction() {
        return action;
    }
    public Map<String, String> getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }
}