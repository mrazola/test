package com.king.test.service;

import java.util.List;

/**
 * TODO Comment
 *
 */
public interface ScoreService {

    public void insertScore(Integer level, Record record);
    
    public List<Record> getHighScoreList(Integer level);
}
