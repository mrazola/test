package com.king.test.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * TODO Comment
 *
 * @author mrazola
 * @created 23 May 2016
 */
public class LinkedListScoreService implements ScoreService {

    private final ReadWriteLock lock;
    private final ConcurrentMap<Integer, Ranking> scoresByLevel;
    
    public LinkedListScoreService() {
        this.lock = new ReentrantReadWriteLock();
        this.scoresByLevel = new ConcurrentHashMap<>();
    }
    
    @Override
    public void insertScore(final Integer level, final Record record) {
        scoresByLevel.putIfAbsent(level, new Ranking());
        Ranking forLevel = scoresByLevel.get(level);
        forLevel.insertScore(record);
    }

    @Override
    public List<Record> getHighScoreList(Integer level) {
        // TODO Auto-generated method stub
        return scoresByLevel.get(level).getRank();
    }

}
