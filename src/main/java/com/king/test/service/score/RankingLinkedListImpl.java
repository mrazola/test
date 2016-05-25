package com.king.test.service.score;

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
public class RankingLinkedListImpl implements Ranking {

    private static final int DEFAULT_MAX_SIZE = 15;
    
    private final ReadWriteLock lock;
    private final Set<Integer> members; // keeps track of members of the ranking, only to help with having a clearer algorithm
    private final LinkedList<Record> rank;
    private final int maxSize;

    public RankingLinkedListImpl() {
        this(DEFAULT_MAX_SIZE);
    }
    
    public RankingLinkedListImpl(int maxSize) {
        this.maxSize = maxSize;
        this.lock = new ReentrantReadWriteLock();
        this.members = new HashSet<>(maxSize * 2); // to avoid rehashing
        this.rank = new LinkedList<>();
    }
    
    /* (non-Javadoc)
	 * @see com.king.test.service.score.Ranking#insertScore(com.king.test.service.score.Record)
	 */
    @Override
	public void insertScore(Record record) {
        
        lock.writeLock().lock();
        try {
            if (rank.isEmpty()) {
                rank.add(record);
                members.add(record.getUid());
            } else {
                if (rank.size() < maxSize || record.getScore() > rank.getLast().getScore()) {
                    
                    if (members.contains(record.getUid())) {
                        this.updateScore(record);
                    } else {
                        this.insertNewScore(record);
                        members.add(record.getUid());
                        if (rank.size() > maxSize) {
                        	Record removed = rank.removeLast();
                        	members.remove(removed.getUid());
                        }
                    }
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
        
        if (!inserted && rank.size() < maxSize) { // we have a new top player... at lowest rank :)
            rank.add(record);
        }
    }
    
    /* (non-Javadoc)
	 * @see com.king.test.service.score.Ranking#getRank()
	 */
    @Override
	public List<Record> getRank() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(rank);
        } finally  {
            lock.readLock().unlock();
        }
    }
}
