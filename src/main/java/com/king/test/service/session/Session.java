package com.king.test.service.session;

import java.io.Serializable;

/**
 * TODO Comment
 *
 * @author mrazola
 * @created 23 May 2016
 */
public class Session implements Serializable {
    
    private static final long serialVersionUID = 8757088461810818775L;
    
    private final Integer uid;
    private final Long expiration;

    public Session(Integer uid, Long expiration) {
        super();
        this.uid = uid;
        this.expiration = expiration;
    }
    public Integer getUid() {
        return uid;
    }

    public Long getExpiration() {
        return expiration;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("uid=").append(uid).append(". expiration=").append(expiration).toString();
    }
    
    
}
