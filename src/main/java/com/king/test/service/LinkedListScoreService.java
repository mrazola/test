package com.king.test.service;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * TODO Comment
 *
 * @author mrazola
 * @created 23 May 2016
 */
public class LinkedListScoreService implements ScoreService {

    private final ConcurrentMap<Integer, List<Record>> scoresByLevel;
    
    public LinkedListScoreService() {
        this.scoresByLevel = new ConcurrentHashMap<>();
    }
    
    @Override
    public void insertScore(final Integer level, final Record record) {
        scoresByLevel.putIfAbsent(level, new LinkedList<>());
        List<Record> forLevel = scoresByLevel.get(level);
        synchronized (forLevel) {
            ListIterator<Record> it = forLevel.listIterator();
            if (!it.hasNext()) {
                it.set(record);
            } else {
                Record last = it.next();
                if (last.getScore() < record.getScore()) {
                    it.remove();
                    while (it.hasNext()) {
                        Record current = it.next();
                        it.
                        if (current.getScore() < record.getScore()) 
                    }
                }
            }
        }
    }

    @Override
    public List<Record> getHighScoreList(Integer level) {
        // TODO Auto-generated method stub
        return null;
    }

}
