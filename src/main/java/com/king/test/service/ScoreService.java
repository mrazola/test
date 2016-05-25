package com.king.test.service;

import java.util.List;

import com.king.test.service.ranking.Record;

/**
 * Main facade for all the features needed by the test.
 *
 */
public interface ScoreService {

	public String login(Integer uid);
	
    public void insertScore(Integer level, String sessionkey, Integer score);
    
    public List<Record> getHighScoreList(Integer level);
}
