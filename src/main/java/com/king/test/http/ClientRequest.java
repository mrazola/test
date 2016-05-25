package com.king.test.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

/**
 * Abstracts an HTTP request to the score server, mapping the url, request params and body
 * into desired fields.
 * Provides a {@code ClientRequestBuilder} to help instantiation.
 *
 */
public class ClientRequest {

    private final Integer id; // first path param
    private final String action; // second path param
    private final Map<String, String> params;
    private final String body;

    private ClientRequest(ClientRequestBuilder builder) {
        if (builder.id == null || builder.action == null) {
            throw new IllegalArgumentException("Could not parse client request");
        }
        this.id = builder.id;
        this.action = builder.action;
        this.params = builder.params;
        this.body = builder.body;
    }

    public static ClientRequestBuilder builder() {
        return new ClientRequestBuilder();
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

    public static class ClientRequestBuilder {

        protected Integer id;
        protected String action;
        protected Map<String, String> params;
        protected String body;

        public ClientRequest build() throws IllegalArgumentException {
            return new ClientRequest(this);
        }

        public ClientRequestBuilder withHttpExchange(HttpExchange exchange) {
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
            } catch (Exception ignore) {

            }
            return this;
        }
        
        public ClientRequestBuilder withId(Integer id) {
            this.id = id;
            return this;
        }
        
        public ClientRequestBuilder withAction(String action) {
            this.action = action;
            return this;
        }
        
        public ClientRequestBuilder withBody(String body) {
            this.body = body;
            return this;
        }
        
        public ClientRequestBuilder withParams(Map<String, String> params) {
            this.params = params;
            return this;
        }
        
        public ClientRequestBuilder addParam(String key, String value) {
            if (this.params == null) {
                this.params = new HashMap<>();
            }
            params.put(key, value);
            return this;
        }
    }

}