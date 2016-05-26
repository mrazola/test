package com.king.test.service.ranking;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This implementation is suitable when ranking size is limited, and max size is small.
 * Ranking limit defaults to {@link RankingLinkedListImpl#DEFAULT_MAX_SIZE}.
 * 
 * The ranking is backed by a {@code LinkedList}; when inserting, the algorithm only iterates the list once,
 * and only if the score to be inserted is greater than current minimum.
 * It also tries to provide a higher throughput by using a {@code ReadWriteLock} to manage concurrent access
 * to data structures.
 * 
 * If ranking is not bounded, or limits are high, an alternative (tree-based) implementation 
 * should be chosen instead.
 *
 */
public class RankingLinkedListImpl implements Ranking {

    public static final int DEFAULT_MAX_SIZE = 15;
    
    private final ReadWriteLock lock;
    private final Set<Integer> members; // keeps track of members of the ranking, only to help with having a clearer algorithm
    private final LinkedList<Record> rank;
    private final int maxSize;

    public RankingLinkedListImpl() {
        this(DEFAULT_MAX_SIZE);
    }
    
    public RankingLinkedListImpl(int maxSize) {
        this.maxSize = maxSize;
        this.lock = new ReentrantReadWriteLock(true); // be fair
        this.members = new HashSet<>(maxSize * 2); // to avoid rehashing
        this.rank = new LinkedList<>();
    }

    @Override
	public void insertScore(Record record) {
        
        lock.writeLock().lock(); // lock for writing
        try {
            if (rank.isEmpty()) {
                rank.add(record);
                members.add(record.getUid());
            } else {
                // only insert if no maxSize reached or score is greater than curernt minimum
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
    
    /**
     * Updates an existing player record in the ranking
     */
    private void updateScore(Record record) {
    	ListIterator<Record> it = rank.listIterator();
    	boolean inserted = false;
    	boolean found = false;
    	/*
         * Iterate until we find the user in the rank, either to:
         * - Do nothing if previous score was greater
         * - Update his score if with the new score she will stay in the same position
         * - Delete previous record if score is lower (so the newer was inserted before in the rank)
         */
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
    
    /**
     * Insert a new record in the ranking
     */
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
        
        if (!inserted && rank.size() < this.maxSize) { // we have a new top player... at lowest rank :)
            rank.add(record);
        }
    }

    @Override
	public List<Record> getTop() {
        lock.readLock().lock(); // only lock for reading here
        try {
            return Collections.unmodifiableList(rank);
        } finally  {
            lock.readLock().unlock();
        }
    }
}
