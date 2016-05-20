package com.king.test.service;

import java.util.List;

/**
 * TODO Comment
 *
 */
public interface ScoreService {

    public void insertScore(Integer level, Integer score);
    
    public List<Record> getHighScoreList(Integer level);
}
