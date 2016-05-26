package com.king.test.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.king.test.service.ranking.Record;

/**
 * Main facade for all the features needed by the test.
 *
 */
public interface ScoreService {

	/**
	 * 
	 * @param uid
	 * @return the sessionkey for the user
	 */
	public String login(Integer uid) throws IllegalArgumentException;
	
	/**
	 * 
	 * @param level
	 * @param sessionkey 
	 * @param score
	 * @throws NoSuchElementException if the corresponding session to sessionkey is not active
	 */
    public void insertScore(Integer level, String sessionkey, Integer score) throws NoSuchElementException, IllegalArgumentException;
    
    /**
     * 
     * @param level
     * @return an ordered list of pairs uid-score for the provided level
     */
    public List<Record> getHighScoreList(Integer level);
}
