package com.king.test.service;

import java.util.List;

import com.king.test.service.ranking.Record;

/**
 * TODO Comment
 *
 */
public interface ScoreService {

	public String login(Integer uid);
	
    public void insertScore(Integer level, String sessionkey, Integer score);
    
    public List<Record> getHighScoreList(Integer level);
}
