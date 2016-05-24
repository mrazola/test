package com.king.test.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO Comment
 *
 * @author mrazola
 * @created 23 May 2016
 */
public class RankingReversed {

    private static final int MAX_SIZE=15;
    
    private final ReadWriteLock lock;
    private final Set<Integer> members;
    private final LinkedList<Record> rank;
    private volatile int minScore = -1;

    public RankingReversed() {
        this.lock = new ReentrantReadWriteLock();
        this.members = new HashSet<>(32);
        this.rank = new LinkedList<>();
    }
    
    public void insertScore(Record record) {
        
        lock.writeLock().lock();
        try {
            if (rank.isEmpty()) {
                rank.add(record);
                members.add(record.getUid());
                this.minScore = record.getScore();
            } else {
                if (rank.size() < MAX_SIZE || record.getScore() > minScore) {
                    
                    if (members.contains(record.getUid())) {
                        this.updateScore(record);
                    } else {
                        this.insertNewScore(record);
                        members.add(record.getUid());
                    }
                    this.minScore = Math.min(this.minScore, record.getScore());
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private void updateScore(Record record) {
    	ListIterator<Record> it = rank.listIterator();
    	boolean inserted = false;
    	boolean found = false;
    	while (it.hasNext() && !found) {
            Record currentBest = it.next();
            if (!inserted && currentBest.getScore() < record.getScore()) {
            	it.previous();
            	it.add(record);
                it.next();
                inserted = true;
            }
            
            if (currentBest.getUid().equals(record.getUid())) {
            	if (inserted) {
            		it.remove(); // user had a greater score and jumped positions
            	} else if (currentBest.getScore() < record.getScore()) {
            		it.set(record); // user has a greater score, same position
            	}
            	found = true; // abort iteration
            }
        }
    }
    
    private void insertNewScore(Record record) {
    	ListIterator<Record> it = rank.listIterator();
    	boolean inserted = false;
        while (it.hasNext() && ! inserted) {
            Record currentBest = it.next();
            if (currentBest.getScore() < record.getScore()) {
                it.previous();
                it.add(record);
                inserted = true;
            }
        }
        if (!inserted) { // we have a new top player... at lowest rank :)
            rank.add(record);
        }
    }
    
    public List<Record> getRank() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(rank);
        } finally  {
            lock.readLock().unlock();
        }
    }
}
