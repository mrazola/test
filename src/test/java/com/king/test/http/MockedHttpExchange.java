package com.king.test.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Mocks {@code HttpExchange} to simulate a custom request. Not intended for a full testing-suite,
 * as it only tracks response code and provides URI + request body.
 *
 */
public class MockedHttpExchange extends HttpExchange {

    URI uri;
    OutputStream out = new ByteArrayOutputStream();
    InputStream in;
    
    private int rCode;
    private long responseLength;
    
    Map<String, Object> attributes;
    
    public MockedHttpExchange() {
        
    }
    
    /**
     * To be called to reset this {@code MockedHttpExchange} to a new state, as if it was a new request.
     */
    public void reset(URI mockedURI, String requestBody) {
        this.uri = mockedURI;
        this.in = new ByteArrayInputStream(requestBody.getBytes());
        this.out = new ByteArrayOutputStream();
        this.attributes = new HashMap<>();
        this.rCode = 0;
        this.responseLength = 0;
    }
    
    @Override
    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException ignore) {}
        
    }

    @Override
    public Object getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpContext getHttpContext() {
        throw new NotImplementedException();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        throw new NotImplementedException();
    }

    @Override
    public HttpPrincipal getPrincipal() {
        throw new NotImplementedException();
    }

    @Override
    public String getProtocol() {
        throw new NotImplementedException();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        throw new NotImplementedException();
    }

    @Override
    public InputStream getRequestBody() {
        return in;
    }

    @Override
    public Headers getRequestHeaders() {
        throw new NotImplementedException();
    }

    @Override
    public String getRequestMethod() {
        throw new NotImplementedException();
    }

    @Override
    public URI getRequestURI() {
        return uri;
    }

    @Override
    public OutputStream getResponseBody() {
        return out;
    }

    @Override
    public int getResponseCode() {
        return rCode;
    }

    @Override
    public Headers getResponseHeaders() {
        Headers headers = new Headers();
        headers.add("Content-Length", Long.valueOf(this.responseLength).toString());
        return headers;
    }

    @Override
    public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
        this.rCode = rCode;
        this.responseLength = responseLength;
    }

    @Override
    public void setAttribute(String name, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStreams(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

}
