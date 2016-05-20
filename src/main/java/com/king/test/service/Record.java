package com.king.test.service;

/**
 * TODO Comment
 *
 */
public class Record {

    public final Integer uid;
    public final Integer score;

    public Record(Integer uid, Integer score) {
        super();
        this.uid = uid;
        this.score = score;
    }
    
    public Integer getUid() {
        return uid;
    }
    public Integer getScore() {
        return score;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append(uid).append('=').append(score).toString();
    }
    
    
}
