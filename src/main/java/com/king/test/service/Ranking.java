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
public class Ranking {

    private static final int MAX_SIZE=15;
    
    private final ReadWriteLock lock;
    private final Set<Integer> members;
    private final LinkedList<Record> rank;

    public Ranking() {
        this.lock = new ReentrantReadWriteLock();
        this.members = new HashSet<>(32);
        this.rank = new LinkedList<>();
    }
    
    public void insertScore(Record record) {
        
        lock.writeLock().lock();
        try {
            ListIterator<Record> it = rank.listIterator();
            if (!it.hasNext()) {
                rank.add(record);
                members.add(record.getUid());
            } else {
                Record last = it.next();
                if (last.getScore() < record.getScore()) {
                    boolean inserted = false;
                    if (members.contains(record.getUid())) {
                        while (it.hasNext() && !inserted) {
                            Record current = it.next();
                            if (current.getUid().equals(record.getUid())) {
                                if (current.getScore() < record.getScore()) {
                                    it.remove();
                                } else {
                                    inserted = true;
                                }
                            }
                            if (current.getScore() >= record.getScore()) {
                                it.set(record);
                                inserted = true;
                            }
                        }
                    } else {
                        if (members.size() >= MAX_SIZE) {
                            it.remove();
                        }
                        while (it.hasNext() && ! inserted) {
                            Record current = it.next();
                            if (current.getScore() >= record.getScore()) {
                                it.previous();
                                it.add(record);
                                members.add(record.getUid());
                                inserted = true;
                            }
                        }
                    }
                    if (!inserted) { // we have a new top 1 player :)
                        it.add(record);
                        members.add(record.getUid());
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
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
